package com.example.demo.audio;

import java.nio.ByteBuffer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

public class AudioHandler implements AudioSendHandler {
	
	private AudioPlayer audioPlayer;
	private AudioFrame lastFrame;
	
	public AudioHandler(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
	}
	
	@Override
	public boolean canProvide() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public ByteBuffer provide20MsAudio() {
		// TODO Auto-generated method stub
		return null;
	}
}
