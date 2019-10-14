package com.zdev.hcf.util;

import org.bukkit.ChatColor;

public class CC {
	public static String translate(final String translateMessage) {
		return ChatColor.translateAlternateColorCodes('&', translateMessage);
	}

	public static String stripColor(final String stripMessage) {
		return ChatColor.stripColor(stripMessage);
	}

	public static String c(final String translateMessage) {
		return ChatColor.translateAlternateColorCodes('&', translateMessage);
	}
}
