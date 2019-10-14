package com.zdev.hcf.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.Base;

public class CoordsCommand implements CommandExecutor {
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (command.getName().equalsIgnoreCase("coords")) {
			for (String msg : Base.config.getStringList("Coords")) {
				commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			}
			return true;
		}
		return false;
	}
}