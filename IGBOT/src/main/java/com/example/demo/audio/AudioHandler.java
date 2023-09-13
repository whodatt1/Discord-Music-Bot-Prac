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
		lastFrame = audioPlayer.provide();
		return lastFrame != null;
	}
	
	@Override
	public ByteBuffer provide20MsAudio() {
		// ByteBuffer : 바이트 데이터를 저장하고 읽는 저장소
		return ByteBuffer.wrap(lastFrame.getData());
	}

	@Override
	public boolean isOpus() {
		// 이 메소드가 true를 반환하면 JDA는 Provide20MsAudio()에서 제공한 오디오 데이터를 Opus 오디오의 사전 인코딩된 20밀리초 패킷으로 처리
		return true;
	}
}
