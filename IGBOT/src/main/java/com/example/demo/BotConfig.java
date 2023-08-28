package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import lombok.Getter;

@Getter
public class BotConfig {
	
	private String token, prefix, helpWord, successEmoji, warningEmoji, errorEmoji, loadingEmoji, searchingEmoji;
	private boolean stayInChannel;
	private long owner;
	
	private boolean valid = false;
	
	public final static Logger LOG = LoggerFactory.getLogger(IGBot.class);
	
	// 초기화
	public void init() {
		
		valid = false;
		
		try {
			// conf 파일 프로퍼티 로드
			Config config = ConfigFactory.load();
			
			token = config.getString("token");
			prefix = config.getString("prefix");
			owner = config.getLong("owner");
			helpWord = config.getString("help");
			successEmoji = config.getString("success");
			warningEmoji = config.getString("warning");
			errorEmoji = config.getString("error");
			loadingEmoji = config.getString("loading");
			searchingEmoji = config.getString("searching");
			stayInChannel = config.getBoolean("stayinchannel");
			
			valid = true;
		} catch (ConfigException e) {
			LOG.error(e + e.getMessage());
		}
		
	}
	
}
