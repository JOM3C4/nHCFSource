package com.zdev.hcf.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class GMCCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("gmc")) {
			if ((!p.hasPermission("command.gamemode"))) {
				p.sendMessage("§cYou do not have permission to run this command!");
				return true;
			}
			if (args.length == 0) {
				if (StaffModeCommand.modMode.contains(sender)) {
					sender.sendMessage(ChatColor.RED + ("You cannot change gamemode while in mod mode."));
				} else {
					if (args.length == 0) {
						p.setGameMode(GameMode.CREATIVE);
					}
				}
			}
			return false;
		}
		return true;
	}

}
