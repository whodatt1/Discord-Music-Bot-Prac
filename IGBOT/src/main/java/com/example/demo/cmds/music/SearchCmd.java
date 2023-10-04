package com.example.demo.cmds.music;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

import com.example.demo.Bot;
import com.example.demo.audio.AudioHandler;
import com.example.demo.audio.GuildMusicManager;
import com.example.demo.cmds.MusicCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.OrderedMenu;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

public class SearchCmd extends MusicCommand {

	protected String searchPrefix = "ytsearch:";
	protected GuildMusicManager musicManager;
	// 해당 커맨드 입력시 생성되는 메뉴
	private final OrderedMenu.Builder builder;
	private final String searchingEmoji;
	
	public SearchCmd(Bot bot) {
		super(bot);
		this.name = "search";
		this.searchingEmoji = bot.getConfig().getSearchingEmoji();
		this.arguments = "<query>";
		this.help = "해당 검색어로 Youtube를 검색합니다.";
		this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
		this.beListening = true;
		this.bePlaying = false;
		builder = new OrderedMenu.Builder()
				.allowTextInput(true)
				.useNumbers()
				.useCancelButton(true)
				.setEventWaiter(bot.getWaiter())
				.setTimeout(1, TimeUnit.MINUTES);
	}
	
	@Override
	public void doCommand(CommandEvent event) {
		
		if (event.getArgs().isEmpty()) {
			event.reply("검색어를 입력해주세요.");
			return;
		}
		
		musicManager = bot.getAudioManager().getGuildMusicManager(event.getGuild());
		
		event.reply(searchingEmoji+" 검색중... `["+event.getArgs()+"]`", 
                m -> bot.getAudioManager().getPlayerManager().loadItemOrdered(event.getGuild(), searchPrefix + event.getArgs(), new ResultHandler(m, event)));
	}
	
	private class ResultHandler implements AudioLoadResultHandler {
		
		private final Message m;
		private final CommandEvent event;
		
		private ResultHandler(Message m, CommandEvent event) {
			this.m = m;
			this.event = event;
		}

		@Override
		public void trackLoaded(AudioTrack track) {
			musicManager.scheduler.queue(track);
			event.replySuccess(track.getInfo().title + " 트랙리스트에 추가");
		}

		@Override
		public void playlistLoaded(AudioPlaylist playlist) {
			builder.setColor(event.getSelfMember().getColor())
				   .setText("검색 결과 `"+event.getArgs()+"`:")
				   .setChoices(new String[0])
				   .setSelection((msg, i) -> {
					   AudioTrack track = playlist.getTracks().get(i-1);
					   musicManager.scheduler.queue(track);
					   
					   GuildMusicManager musicManager = bot.getAudioManager().getGuildMusicManager(event.getGuild());
					   Queue<AudioTrack> q = musicManager.scheduler.getQueue();
					   
					   event.replySuccess(track.getInfo().title + (q.isEmpty() ? " 재생 중" : " 추가 완료"));
				   })
				   .setCancel((msg) -> {})
				   .setUsers(event.getAuthor())
				   ;
			for (int i = 0; i < 4 && i < playlist.getTracks().size(); i++) {
				AudioTrack track = playlist.getTracks().get(i);
				builder.addChoice("`["+track.getDuration()+"]` [**"+track.getInfo().title+"**]("+track.getInfo().uri+")");
			}
			builder.build().display(m);
		}

		@Override
		public void noMatches() {
			m.editMessage(event.getClient().getWarning() +event.getArgs()+ " 에 대한 검색결과가 없습니다.").queue();
		}

		@Override
		public void loadFailed(FriendlyException exception) {
			event.replyError("재생할 수 없습니다: " + exception.getMessage());
		}
		
	}
}
