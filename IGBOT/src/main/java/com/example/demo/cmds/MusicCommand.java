package com.example.demo.cmds;

import com.example.demo.Bot;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.managers.AudioManager;

public abstract class MusicCommand extends Command {
	
	protected final Bot bot;
    protected boolean bePlaying;
    protected boolean beListening;
	
	// 생성자
	public MusicCommand (Bot bot) {
		this.bot = bot;
		this.guildOnly = true;
		this.category = new Category("Music");
	}
	
	@Override
	protected void execute(CommandEvent event) {
		TextChannel tChannel = event.getTextChannel();
		// 텍스트 채널이 아닐시
		if(tChannel == null) {
			try {
				event.getMessage().delete().queue();
			} catch (PermissionException e) {}
			
			event.replyInDm("텍스트 채널 안에서 명령어를 입력하세요.");
			return;
		}
		
		Guild guild = event.getGuild();
		
		// 길드 정보가 있으면
		if (guild != null) {
			doCommand(event);
		}
		
	}

	public abstract void doCommand(CommandEvent event);
}
