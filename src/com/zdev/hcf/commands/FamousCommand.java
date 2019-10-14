package com.zdev.hcf.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.Base;

public class FamousCommand implements CommandExecutor {
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (command.getName().equalsIgnoreCase("famous")) {
			for (String msg : Base.config.getStringList("Famous")) {
				commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			}
			return true;
		}
		return false;
	}
}