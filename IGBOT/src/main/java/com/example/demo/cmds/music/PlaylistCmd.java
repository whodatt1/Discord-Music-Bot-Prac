package com.example.demo.cmds.music;


import com.example.demo.Bot;
import com.example.demo.cmds.MusicCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

public class PlaylistCmd extends MusicCommand {
	
	public PlaylistCmd(Bot bot) {
		super(bot);
		this.name = "playlist";
		this.help = "현재 트랙리스트를 조회합니다.";
	}

	@Override
	public void doCommand(CommandEvent event) {
		
	}
}
