package com.example.demo.audio;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;

@Getter
public class AudioManager {
	
	private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;
	
    public AudioManager() {
        this.musicManagers = new HashedMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        
        AudioSourceManagers.registerRemoteSources(playerManager);
//        AudioSourceManagers.registerLocalSource(playerManager);
    }
    
    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
    	long guildId = Long.parseLong(guild.getId());
    	GuildMusicManager musicManager = musicManagers.get(guildId);
    	
    	if (musicManager == null) {
    		musicManager = new GuildMusicManager(playerManager);
    		musicManagers.put(guildId, musicManager);
    	}
    	
    	guild.getAudioManager().setSendingHandler(musicManager.getAudioHandler());
    	
    	return musicManager;
    }
    
    public String getTimeStamp(long milliseconds) {
    	int seconds = (int) (milliseconds / 1000) % 60;
    	int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
    	int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
    	
    	if (hours > 0) {
    		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    	} else {
    		return String.format("%02d:%02d", hours, minutes);
    	}
    }
}
