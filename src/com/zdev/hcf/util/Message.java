package com.zdev.hcf.util;

import net.md_5.bungee.api.ChatColor;

public enum Message {

	NO_PERMISSION(ChatColor.RED + "No permission."), ARROW_PREFIX(ChatColor.GOLD + ""),
	ARROW_SINGLE(ChatColor.DARK_GRAY + " » "),
	CLEAR_INVENTORY(ChatColor.GOLD + "You have cleared the inventory of " + ChatColor.RESET + "%player%"
			+ ChatColor.GOLD + "."),
	TELEPORT_MESSAGE(ChatColor.GOLD + "Teleported to " + ChatColor.WHITE + ChatColor.GOLD.toString() + ChatColor.BOLD
			+ "%player%"),
	OFFLINE_PLAYER(ChatColor.RED + "That player is not currently online."),
	OPENING_CHEST_SILENTY(ChatColor.YELLOW + "Opening chest silenty..");

	private final String message;

	Message(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return this.message;
	}

}
