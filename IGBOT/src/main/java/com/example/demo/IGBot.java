package com.example.demo;

import java.util.Arrays;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.cmds.music.ExitCmd;
import com.example.demo.cmds.music.PlayCmd;
import com.example.demo.cmds.music.PlaylistCmd;
import com.example.demo.cmds.music.RepeatCmd;
import com.example.demo.cmds.music.SearchCmd;
import com.example.demo.cmds.music.ShuffleCmd;
import com.example.demo.cmds.music.SkipCmd;
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
		
		Bot bot = new Bot(config, waiter);
		
		CommandClientBuilder cb = new CommandClientBuilder()
				.setPrefix(config.getPrefix())
				.setOwnerId(Long.toString(config.getOwner()))
				.setEmojis(config.getSuccessEmoji(), config.getWarningEmoji(), config.getErrorEmoji())
				.setHelpWord(config.getHelpWord())
				.setLinkedCacheSize(200)
				.addCommands(
						// 여기에 커맨드 클래스 만들어서 넣기
						
						// Music
						new SearchCmd(bot)
						, new SkipCmd(bot)
						, new ExitCmd(bot)
						, new RepeatCmd(bot)
						, new ShuffleCmd(bot)
						, new PlayCmd(bot)
						, new PlaylistCmd(bot)
						);
		
		try {
			// JDA
			JDA jda = JDABuilder
					.create(config.getToken(), Arrays.asList(INTENTS))
					.enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
					.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE, CacheFlag.ONLINE_STATUS)
					.addEventListeners(cb.build(), waiter)
					.setActivity(Activity.playing("두근두근세근네근"))
					.build();
			
		} catch (LoginException e) {
			
			LOG.error("토큰이 올바른지 확인하세요.");
			
		} catch (IllegalArgumentException e) {
			
			LOG.error("특정 설정이 올바르지 않습니다.");
			
		} catch (ErrorResponseException e) {
			
			LOG.error("인터넷 연결을 확인하세요.");
			
		}
	}
	
}
