package com.zdev.hcf.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messager {
	public static void player(final Player player, final String msg) {
		player.sendMessage(CC.translate(msg));
	}

	public static void console(final String msg) {
		Bukkit.getConsoleSender().sendMessage(CC.translate(msg));
	}

	public static void broadcast(final String msg) {
		Bukkit.broadcastMessage(CC.translate(msg));
	}

	public static void warn(final String msg) {
		Bukkit.getLogger().warning(msg);
	}

	public static void commandSender(final CommandSender sender, final String string) {
		sender.sendMessage(CC.translate(string));
	}

	public static void notFound(final Player player, final String string) {
		player(player, "&cPlayer '&f" + string + "&c' not found!");
	}

}
