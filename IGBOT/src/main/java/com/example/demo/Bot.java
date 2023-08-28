package com.example.demo;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import lombok.Getter;

@Getter
public class Bot {
	
	private final EventWaiter waiter;
	private final BotConfig config;
	
	private boolean shutdown = false;
	
	public Bot(EventWaiter waiter, BotConfig config) {
		this.waiter = waiter;
		this.config = config;
	}
	
}
