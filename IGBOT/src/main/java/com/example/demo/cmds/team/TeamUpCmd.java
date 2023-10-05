package com.example.demo.cmds.team;

import java.util.Collections;
import java.util.List;

import com.example.demo.Bot;
import com.example.demo.cmds.TeamCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class TeamUpCmd extends TeamCommand {
	
	private final String loadingEmoji;
	
	public TeamUpCmd(Bot bot) {
		super(bot);
		this.loadingEmoji = bot.getConfig().getLoadingEmoji();
		this.name = "teamup";
		this.help = "현재 참여하고 있는 음성채널 인원을 랜덤으로 돌려 두 팀을 구성합니다.";
	}

	@Override
	public void doCommand(CommandEvent event) {
		
		// 사용자가 음성채널에 있는 경우에만 실행
		GuildVoiceState userState = event.getMember().getVoiceState();
		VoiceChannel voiceChannel = userState.getChannel();
		
		// 현재 사용자가 음성채널에 연결되어 있는지 확인
		if (!userState.inVoiceChannel() || userState.isDeafened()) {
			if (voiceChannel == null) {
				event.replyError("당신은 현재 보이스 채널에 연결되어있지 않습니다. 보이스 채널에 입장 해주세요.");
			}
			return;
		}
		
		List<Member> members = voiceChannel.getMembers();
		
		if (members.size() % 2 != 0) {
			event.replyError("현재 보이스채널의 인원이 짝수인 경우만 가능합니다. 짝수로 인원을 맞춰주세요.");
			return;
		}
		
		// 멤버 섞기
		Collections.shuffle(members);
		
		
	}
}
