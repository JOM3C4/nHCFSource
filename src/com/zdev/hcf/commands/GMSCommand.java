package com.zdev.hcf.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GMSCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("gms")) {
			if ((!p.hasPermission("command.gamemode"))) {
				p.sendMessage("§cYou do not have permission to run this command!");
				return true;
			}
			if (args.length == 0) {
				p.setGameMode(GameMode.SURVIVAL);
			}
		}
		return false;
	}

}
