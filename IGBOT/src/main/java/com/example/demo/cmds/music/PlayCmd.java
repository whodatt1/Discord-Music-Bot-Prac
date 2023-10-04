package com.example.demo.cmds.music;

import com.example.demo.Bot;
import com.example.demo.audio.GuildMusicManager;
import com.example.demo.cmds.MusicCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Message;

public class PlayCmd extends MusicCommand {
	private final static String LOAD = "\uD83D\uDCE5";
	private final static String CANCEL = "\uD83D\uDEAB";
	
	protected GuildMusicManager musicManager;
	private final String loadingEmoji;
	
	public PlayCmd(Bot bot) {
		super(bot);
		this.loadingEmoji = bot.getConfig().getLoadingEmoji();
		this.name = "play";
		//기본 도움말 빌더에 사용되는 명령에 대한 인수 형식 문자열
		this.arguments = "<title|URL|subcommand>";
		this.help = "URL을 통해 제공된 노래를 재생합니다.";
	}

	@Override
	public void doCommand(CommandEvent event) { 
		if (event.getArgs().isEmpty()) {
			event.reply("제목 혹은 URL 입력해주세요.");
			return;
		}
		
		event.reply(loadingEmoji + " 로딩중... `["+event.getArgs()+"]`", m -> bot.getAudioManager().getPlayerManager().loadItemOrdered(event.getGuild(), event.getArgs(), new ResultHandler(m, event, false)));
	}
	
	private class ResultHandler implements AudioLoadResultHandler {
		private final Message m;
		private final CommandEvent event;
		private final boolean ytsearch;
		
		private ResultHandler(Message m, CommandEvent event, boolean ytsearch) {
			this.m = m;
			this.event = event;
			this.ytsearch = ytsearch;
		}

		@Override
		public void trackLoaded(AudioTrack track) {
			musicManager.scheduler.queue(track);
			event.replySuccess(track.getInfo().title + " 트랙리스트에 추가");
		}

		@Override
		public void playlistLoaded(AudioPlaylist playlist) {
			AudioTrack firstTrack = playlist.getSelectedTrack();
			
			System.out.println(firstTrack.getIdentifier());
			
			if (firstTrack == null) {
				firstTrack = playlist.getTracks().get(0);
			}
			
			event.replySuccess(firstTrack.getInfo().title + " 트랙리스트에 추가 ");
			
			musicManager.scheduler.queue(firstTrack);
		}

		@Override
		public void noMatches() {
			if (ytsearch) {
				event.replyError(event.getArgs() + " 찾지 못함");
			} else {
				bot.getAudioManager().getPlayerManager().loadItemOrdered(event.getGuild(), "ytsearch:" + event.getArgs(), new ResultHandler(m, event, true));
			}
		}

		@Override
		public void loadFailed(FriendlyException exception) {
			event.replyError("재생할 수 없습니다 : " + exception.getMessage());
		}
		
		
	}
}
