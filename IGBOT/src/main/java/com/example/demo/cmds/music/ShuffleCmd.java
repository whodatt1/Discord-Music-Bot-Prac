package com.example.demo.cmds.music;

import com.example.demo.Bot;
import com.example.demo.audio.GuildMusicManager;
import com.example.demo.cmds.MusicCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

public class ShuffleCmd extends MusicCommand {
	public ShuffleCmd(Bot bot) {
		super(bot);
		this.name = "shuffle";
		this.help = "플레이리스트를 섞습니다.";
	}

	@Override
	public void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = bot.getAudioManager().getGuildMusicManager(event.getGuild());
		
		if (musicManager.scheduler.getQueue().isEmpty()) {
			event.getChannel().sendMessage("플레이 리스트가 비어있습니다.").queue();
			return;
		}
		
		musicManager.scheduler.shuffle();
		event.getChannel().sendMessage("플레이리스트가 섞였습니다.").queue();
	}
}
