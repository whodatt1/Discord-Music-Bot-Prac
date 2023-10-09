package com.example.demo.cmds.team;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.demo.Bot;
import com.example.demo.cmds.TeamCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.Paginator;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;

public class TeamUpCmd extends TeamCommand {
	
	private final String loadingEmoji;
	private final Paginator.Builder aBuilder;
	private final Paginator.Builder bBuilder;
	
	public TeamUpCmd(Bot bot) {
		super(bot);
		this.loadingEmoji = bot.getConfig().getLoadingEmoji();
		this.name = "teamup";
		this.help = "현재 참여하고 있는 음성채널 인원을 랜덤으로 돌려 두 팀을 구성합니다.";
		aBuilder = new Paginator.Builder()
				   .setColumns(1)
				   .setFinalAction(m -> {try{m.clearReactions().queue();}catch(PermissionException ignore){}})
				   .wrapPageEnds(true)
				   .allowTextInput(true)
	               .useNumberedItems(true)
	               .showPageNumbers(true)
	               .setItemsPerPage(10)
	               .setEventWaiter(bot.getWaiter())
	               .setTimeout(1, TimeUnit.MINUTES);
		bBuilder = new Paginator.Builder()
				.setColumns(1)
				.setFinalAction(m -> {try{m.clearReactions().queue();}catch(PermissionException ignore){}})
				.wrapPageEnds(true)
				.allowTextInput(true)
				.useNumberedItems(true)
				.showPageNumbers(true)
				.setItemsPerPage(10)
				.setEventWaiter(bot.getWaiter())
				.setTimeout(1, TimeUnit.MINUTES);
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
		} else {
			
			int pageNo = 1;
			try {
				System.out.println(event.getArgs());
				pageNo = Integer.parseInt(event.getArgs());
			} catch (Exception ignore) {}
			
			// 멤버 섞기
			Collections.shuffle(members);
			
			int center = members.size() / 2;
			
			String[] teamA = new String[center];
			String[] teamB = new String[center];
			
			for (int i = 0; i < center; i++) {
				teamA[i] = members.get(i).getAsMention();
				teamB[center + i] = members.get(center + i).getAsMention();
			}
			
			aBuilder.setItems(teamA)
					.setText("―――――――――――――― A Team ――――――――――――――")
					.setColor(event.getSelfMember().getColor())
					.setUsers(event.getAuthor());
			
			bBuilder.setItems(teamB)
					.setText("―――――――――――――― B Team ――――――――――――――")
					.setColor(event.getSelfMember().getColor())
					.setUsers(event.getAuthor());
			
			aBuilder.build().paginate(event.getChannel(), pageNo);
			bBuilder.build().paginate(event.getChannel(), pageNo);
		}
	}
}
