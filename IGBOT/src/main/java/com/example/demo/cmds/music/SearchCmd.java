package com.example.demo.cmds.music;

import java.util.concurrent.TimeUnit;

import com.example.demo.Bot;
import com.example.demo.cmds.MusicCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.OrderedMenu;

import net.dv8tion.jda.api.Permission;

public class SearchCmd extends MusicCommand {

	protected String searchPrefix = "ytsearch:";
	// 해당 커맨드 입력시 생성되는 메뉴
	private final OrderedMenu.Builder builder;
	private final String searchingEmoji;
	
	public SearchCmd(Bot bot) {
		super(bot);
		this.searchingEmoji = bot.getConfig().getSearchingEmoji();
		this.arguments = "<query>";
		this.help = "해당 검색어로 Youtube를 검색합니다.";
		this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
		this.beListening = true;
		this.bePlaying = false;
		builder = new OrderedMenu.Builder()
				.allowTextInput(true)
				.useNumbers()
				.useCancelButton(true)
				.setEventWaiter(bot.getWaiter())
				.setTimeout(1, TimeUnit.MINUTES);
	}
	
	@Override
	public void doCommand(CommandEvent event) {
		
		if (event.getArgs().isEmpty()) {
			event.reply("검색어를 입력해주세요.");
			return;
		}
	}
	
}
