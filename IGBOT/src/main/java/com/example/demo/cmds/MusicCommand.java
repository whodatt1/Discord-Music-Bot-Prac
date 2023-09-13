package com.example.demo.cmds;

import com.example.demo.Bot;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;

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
		
		// 여기에 오디오 채널 커넥션 추가하기
		GuildVoiceState userState = event.getMember().getVoiceState();
		VoiceChannel voiceChannel = userState.getChannel();
		
		// 현재 사용자가 음성채널에 연결되어 있는지 확인
		if (!userState.inVoiceChannel() || userState.isDeafened()) {
			if (voiceChannel == null) {
				event.replyError("당신은 현재 보이스 채널에 연결되어있지 않습니다. 보이스 채널에 입장 해주세요.");
			}
			return;
		}
		
		try {
			event.getGuild().getAudioManager().openAudioConnection(userState.getChannel());
		} catch (PermissionException e) {
			event.reply(event.getClient().getError() + userState.getChannel().getName() + "에 연결할 수 없습니다!");
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
