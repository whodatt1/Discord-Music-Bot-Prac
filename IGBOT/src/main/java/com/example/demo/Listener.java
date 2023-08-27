package com.example.demo;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {
	
	private final BotConfig config;
	
	public Listener (BotConfig config) {
		this.config = config;
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String prefix = config.getPrefix();
        String raw = event.getMessage().getContentRaw();
        if(event.isFromGuild()){
            if(raw.equalsIgnoreCase(prefix + "shutdown") && event.getAuthor().getId().equals(Long.toString(config.getOwner()))){
                event.getChannel().sendMessage("영업 종료").queue();
                event.getJDA().shutdown();
                
            }
        }
    }
}
