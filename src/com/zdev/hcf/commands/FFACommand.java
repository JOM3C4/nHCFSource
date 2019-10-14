package com.zdev.hcf.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FFACommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.hasPermission("command.ffa.bypass")) {
				p.sendMessage(ChatColor.RED + "Players have been given the FFA potion effects.");
			} else {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
				p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0));
				p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
				p.sendMessage(ChatColor.GREEN.toString() + "You have been given potion effects for the FFA.");
			}
		}
		sender.sendMessage(ChatColor.GREEN + "You have given all players potion effects.");
		return true;
	}
}
