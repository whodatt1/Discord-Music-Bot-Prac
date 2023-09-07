package com.example.demo;

import com.example.demo.audio.AudioManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import lombok.Getter;

@Getter
public class Bot {
	
	private final EventWaiter waiter;
	private final BotConfig config;
	private final AudioManager audioManager;
	
	private boolean shutdown = false;
	
	public Bot(BotConfig config, EventWaiter waiter) {
		this.config = config;
		this.waiter = waiter;
		this.audioManager = new AudioManager();
	}
	
}
