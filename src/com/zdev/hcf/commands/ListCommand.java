package com.zdev.hcf.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("list")) {
			if (!(sender instanceof Player)) {
				return true;
			}
			sender.sendMessage(ChatColor.AQUA + "There is " + ChatColor.RESET + Bukkit.getOnlinePlayers().length
					+ ChatColor.AQUA + " out of " + ChatColor.RESET + Bukkit.getMaxPlayers() + ChatColor.AQUA
					+ " connected to the server.");
		}
		return true;
	}

}
