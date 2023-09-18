package com.example.demo.cmds.music;

import com.example.demo.Bot;
import com.example.demo.audio.GuildMusicManager;
import com.example.demo.cmds.MusicCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.VoiceChannel;

public class ExitCmd extends MusicCommand {
	public ExitCmd(Bot bot) {
		super(bot);
		this.name = "exit";
		this.help = "봇이 종료됩니다";
	}

	@Override
	public void doCommand(CommandEvent event) {
		VoiceChannel connected = event.getGuild().getSelfMember().getVoiceState().getChannel();
		
		if (connected == null) {
			event.replyError("보이스 채널에 연결되어있는 상태이어야 합니다.");
			return;
		}
		
		GuildMusicManager musicManager = bot.getAudioManager().getGuildMusicManager(event.getGuild());
		// 채널과 연결 종료
		event.getGuild().getAudioManager().closeAudioConnection();
		// 유저에게 알림
		event.reply("보이스 채널과의 연결을 종료합니다.");
		musicManager.scheduler.clearQueue();
		musicManager.scheduler.skip();
	}
	
	
}
