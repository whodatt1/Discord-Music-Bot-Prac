package com.example.demo.audio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackScheduler extends AudioEventAdapter {
	private boolean repeating = false;
	private final AudioPlayer player;
	private final BlockingQueue<AudioTrack> queue;
	private AudioTrack lastTrack;
	
	public TrackScheduler(AudioPlayer player) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
	}
	
	// 대기열에 아무것도 없을시 즉시 재생 있다면 추가
	public void queue(AudioTrack track) {
		if (!player.startTrack(track, true)) {
			queue.offer(track);
		}
	}
	
	// 노래 스킵
	public void skip() {
		player.startTrack(queue.poll(), false);
	}
	
	// 종료시 큐 클리어
	public void clearQueue() {
		queue.clear();
	}
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		this.lastTrack = track;
		
		if (endReason.mayStartNext) {
			// 반복 허용시
			if (repeating) {
				player.startTrack(lastTrack.makeClone(), false);
			} else {
				skip();
			}
		}
	}
	
	// 노래 섞기
	public void shuffle() {
		List<AudioTrack> list = new ArrayList<>(queue);
		queue.clear();
		Collections.shuffle(list);
		for (AudioTrack audioTrack : list) {
			queue.add(audioTrack);
		}
	}
}
