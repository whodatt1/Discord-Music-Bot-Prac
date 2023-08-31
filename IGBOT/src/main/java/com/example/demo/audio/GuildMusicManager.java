package com.example.demo.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
	
	// 길드 오디오 플레이어
	public final AudioPlayer player;
	
	// 플레이어 스케쥴러
	public final TrackScheduler scheduler;
	
	// 길드 오디오 플레이어, 플레이어 스케쥴러 생성
	public GuildMusicManager(AudioPlayerManager manager) {
		player = manager.createPlayer();
		scheduler = new TrackScheduler(player);
		player.addListener(scheduler);
	}
	
	public AudioHandler getAudioHandler() {
		return new AudioHandler(player);
	}
}
