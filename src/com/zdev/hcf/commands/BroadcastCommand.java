package com.zdev.hcf.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BroadcastCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("broadcast")) {
			String r = "";
			if (sender.hasPermission("command.broadcast")) {
				if (args.length > 0) {
					for (int i = 0; i < args.length; i++) {
						r = r + args[i] + " ";
					}
					r = r.replace("&", "�");
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', r));
				} else {
					sender.sendMessage(ChatColor.RED + "Usage: /broadcast <message>");
				}
			} else {
				sender.sendMessage("�cYou lack the sufficient permissions to execute this command.");
			}
		}
		return true;
	}

}
