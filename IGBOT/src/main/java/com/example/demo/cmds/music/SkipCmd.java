package com.example.demo.cmds.music;

import com.example.demo.Bot;
import com.example.demo.audio.GuildMusicManager;
import com.example.demo.cmds.MusicCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

public class SkipCmd extends MusicCommand {
	
	public SkipCmd(Bot bot) {
		super(bot);
		this.name = "skip";
		this.help = "다음 트랙으로 스킵합니다.";
	}

	@Override
	public void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = bot.getAudioManager().getGuildMusicManager(event.getGuild());
		musicManager.scheduler.skip();
		
		event.replySuccess("다음 트랙으로 스킵합니다.");
	}
}
