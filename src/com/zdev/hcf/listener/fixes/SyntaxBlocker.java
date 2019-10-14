package com.zdev.hcf.listener.fixes;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class SyntaxBlocker implements Listener {

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		String[] withArguments = event.getMessage().split(" ");
		String command = withArguments[0].substring(1);

		if (command.contains(":") && !event.getPlayer().hasPermission("bypass.syntaxblocked")) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis syntax is blocked."));
		}
	}
}
