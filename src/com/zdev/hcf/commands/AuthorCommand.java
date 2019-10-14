package com.zdev.hcf.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AuthorCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String c, String[] args) {
		sender.sendMessage(ChatColor.RED + "Core DeveloptMent zDev_ From contact: @xByrus");
		return true;
	}

}
