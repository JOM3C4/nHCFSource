package com.zdev.hcf.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.listener.VanishListener;

public class VanishCommand implements CommandExecutor {

	public static ArrayList<Player> staff = new ArrayList<Player>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("vanish")) {
			if (!sender.hasPermission("command.vanish")) {
				sender.sendMessage(ChatColor.RED + "You lack the sufficient permissions to execute this command.");
				return true;
			}
			if (args.length < 1) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.RED + "You must be a player to execute this command");
					return true;
				}
				Player p = (Player) sender;
				VanishListener.getInstance();
				if (VanishListener.isVanished(p.getPlayer())) {
					VanishListener.getInstance().setVanish(p, false);
					sender.sendMessage(
							ChatColor.translateAlternateColorCodes('&', "&eYou have turned your vanish &coff&e."));
					return true;
				}
				VanishListener.getInstance().setVanish(p, true);
				sender.sendMessage(
						ChatColor.translateAlternateColorCodes('&', "&eYou have turned your vanish &aon&e."));
				return true;
			}
			if (!sender.hasPermission("command.vanish.others")) {
				sender.sendMessage(ChatColor.RED + "No.");
				return true;
			}
			Player t = Bukkit.getPlayer(args[0]);
			if (t == null) {
				sender.sendMessage("�6Could not find player �f" + args[0].toString() + "�6.");
				return true;
			}
			VanishListener.getInstance();
			if (VanishListener.isVanished(t.getPlayer())) {
				VanishListener.getInstance().setVanish(t, false);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&7You have &cdisabled &7" + t.getName() + "'s &3&lVanish Mode"));
				return true;
			}
			VanishListener.getInstance().setVanish(t, true);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&7You have &aenabled &7" + t.getName() + "'s &3&lVanish Mode"));
			return true;
		}
		return false;
	}

}
