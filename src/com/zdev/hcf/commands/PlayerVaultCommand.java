package com.zdev.hcf.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.ConfigurationService;

public class PlayerVaultCommand implements CommandExecutor {
	private final Base plugin;

	public PlayerVaultCommand(Base plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Twat.");
			return false;
		}

		if (!ConfigurationService.KIT_MAP) {
			sender.sendMessage(ChatColor.RED + "This commands can be executed on Kits only.");
			return true;
		}

		Player player = (Player) sender;
		Player target = player;
		if (player.hasPermission("commands.pv.others") && args.length != 0) {
			target = Bukkit.getPlayer(args[0]);

			if (target == null) {
				player.sendMessage(ChatColor.RED + "Unknown player.");
				return true;
			}
		}

		player.openInventory(target.getEnderChest());
		player.sendMessage(ChatColor.WHITE + "Opened " + ChatColor.RED.toString() + ChatColor.BOLD
				+ (player.equals(target) ? "your" : target.getName() + "'s") + ChatColor.WHITE + " player vault.");

		return true;
	}
}