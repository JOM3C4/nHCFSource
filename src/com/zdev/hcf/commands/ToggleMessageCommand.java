package com.zdev.hcf.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleMessageCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("command.togglemessage")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(MessageEvent.format("&cOnly in-game player can execute this command."));
				return false;
			}
			Player player = (Player) sender;
			if (!MessageCommand.toggle.contains(player.getName())) {
				player.sendMessage(MessageEvent.format("&eYou have turned private messages &coff&e."));
				MessageCommand.toggle.add(sender.getName());
				return true;
			}
			if (MessageCommand.toggle.contains(player.getName())) {
				player.sendMessage(MessageEvent.format("&eYou have turned private messages &aon&e."));
				MessageCommand.toggle.remove(sender.getName());
				return true;
			}
			if (args.length > 1) {
				player.sendMessage(MessageEvent.format("&cYou have provided to many arguments"));
			}
		} else {
			sender.sendMessage(MessageEvent.format("&cYou lack the sufficient permissions to execute this command."));
		}
		return false;
	}
}
