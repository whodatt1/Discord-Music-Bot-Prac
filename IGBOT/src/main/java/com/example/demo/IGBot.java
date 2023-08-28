package com.example.demo;

import java.util.Arrays;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class IGBot {
	
	public final static Logger LOG = LoggerFactory.getLogger(IGBot.class);
	public final static GatewayIntent[] INTENTS = {GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES};
	
	public static void main(String[] args) {
		
		BotConfig config = new BotConfig();
		// config 세팅 초기화
		config.init();
		if (!config.isValid()) {
			return;
		}
		
		// 리스너
		EventWaiter waiter = new EventWaiter();
		
		CommandClientBuilder cb = new CommandClientBuilder()
				.setPrefix(config.getPrefix())
				.setOwnerId(Long.toString(config.getOwner()))
				.setEmojis(config.getSuccessEmoji(), config.getWarningEmoji(), config.getErrorEmoji())
				.setHelpWord(config.getHelpWord())
				.useDefaultGame()
				.setLinkedCacheSize(200)
				.addCommands(
						// 여기에 커맨드 클래스 만들어서 넣기
						);
		
		try {
			// JDA
			JDA jda = JDABuilder.create(config.getToken(), Arrays.asList(INTENTS))
					.enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
					.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE, CacheFlag.ONLINE_STATUS)
					.setActivity(Activity.playing("머기"))
					.addEventListeners(cb.build(), waiter)
					.setBulkDeleteSplittingEnabled(true)
					.build();
			
		} catch (LoginException e) {
			
			LOG.error("토큰이 올바른지 확인하세요.");
			
		} catch (IllegalArgumentException e) {
			
			LOG.error("특정 설정이 올바르지 않습니다.");
			
		} catch (ErrorResponseException e) {
			
			LOG.error("인터넷 연결의 확인하세요.");
			
		}
	}
	
}
