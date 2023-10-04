package com.example.demo.cmds.music;


import java.util.Queue;
import java.util.concurrent.TimeUnit;

import com.example.demo.Bot;
import com.example.demo.audio.GuildMusicManager;
import com.example.demo.cmds.MusicCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.Paginator;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.exceptions.PermissionException;

public class PlaylistCmd extends MusicCommand {
	
	private final Paginator.Builder builder;
	
	public PlaylistCmd(Bot bot) {
		super(bot);
		this.name = "playlist";
		this.help = "현재 트랙리스트를 조회합니다.";
		this.arguments = "[pagenum]";
		this.botPermissions = new Permission[]{Permission.MESSAGE_ADD_REACTION,Permission.MESSAGE_EMBED_LINKS};
		builder = new Paginator.Builder()
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
		
		int pageNo = 1;
		try {
			System.out.println(event.getArgs());
			pageNo = Integer.parseInt(event.getArgs());
		} catch (Exception ignore) {}
		
		GuildMusicManager musicManager = bot.getAudioManager().getGuildMusicManager(event.getGuild());
		
		Queue<AudioTrack> q = musicManager.scheduler.getQueue();
		
		if (q.isEmpty()) {
			event.reply("트랙리스트가 비어있습니다!");
			return;
		}
		
		String[] songs = new String[q.size()];
		
		int trackCount = 0;
		
		for (AudioTrack track : q) {
			songs[trackCount] = "`["+track.getDuration()+"]` " + track.getInfo().title;
		}
		
		builder.setItems(songs)
			   .setText(String.valueOf(q.size()) + " 개의 " + "트랙 리스트 : ")
			   .setColor(event.getSelfMember().getColor())	
		       .setUsers(event.getAuthor());

 		builder.build().paginate(event.getChannel(), pageNo);
	}
}
