package com.example.demo.cmds.music;

import com.example.demo.Bot;
import com.example.demo.audio.GuildMusicManager;
import com.example.demo.audio.TrackScheduler;
import com.example.demo.cmds.MusicCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

public class RepeatCmd extends MusicCommand {
	public RepeatCmd(Bot bot) {
		super(bot);
		this.name = "repeat";
		this.help = "플레이어를 반복재생 시킵니다.";
	}

	@Override
	public void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = bot.getAudioManager().getGuildMusicManager(event.getGuild());
		TrackScheduler scheduler = musicManager.scheduler;
		
		scheduler.setRepeating(!scheduler.isRepeating());
		event.reply("플레이어 " + (scheduler.isRepeating() ? "반복재생 활성화" : "반복재생 비활성화"));
	}
}
