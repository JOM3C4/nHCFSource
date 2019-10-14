package com.zdev.hcf.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.zdev.hcf.BasePlugins;

import net.md_5.bungee.api.ChatColor;

public class FiltersCommand implements CommandExecutor, Listener {

	@SuppressWarnings("unused")
	private static HashMap<UUID, Material> blockfilter = new HashMap<UUID, Material>();

	private static String colour(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This is a Player only command");
			return true;
		}
		Player player = (Player) sender;
		if (args.length < 2) {
			this.printDetail(player);
			return true;
		}

		if (args[0].equalsIgnoreCase("add")) {
			if (BasePlugins.getPlugin().getItemDb().getItem(args[1]) == null) {
				player.sendMessage(colour("&cItem or ID not found."));
				return true;
			}
			addFilter(BasePlugins.getPlugin().getItemDb().getItem(args[1]).getType(), player);
			player.sendMessage(colour("&cYou have added "
					+ BasePlugins.getPlugin().getItemDb().getName(BasePlugins.getPlugin().getItemDb().getItem(args[1]))
					+ " to your filter."));
			return true;
		}
		if (args[0].equalsIgnoreCase("remove")) {
			if (BasePlugins.getPlugin().getItemDb().getItem(args[1]) == null) {
				player.sendMessage(colour("&cItem or ID not found."));
				return true;
			}
			removeFilter(BasePlugins.getPlugin().getItemDb().getItem(args[1]).getType(), player);
			player.sendMessage(colour("&cYou have removed "
					+ BasePlugins.getPlugin().getItemDb().getName(BasePlugins.getPlugin().getItemDb().getItem(args[1]))
					+ " from your filter."));
			return true;
		}
		return false;
	}

	private void addFilter(Material type, Player player) {
		// TODO Auto-generated method stub

	}

	private void removeFilter(Material type, Player player) {
		// TODO Auto-generated method stub

	}

	public void printDetail(Player player) {
		player.sendMessage(colour("&8&m-----------------------"));
		player.sendMessage(ChatColor.YELLOW + " /filter add <material>" + ChatColor.GOLD + " » " + ChatColor.WHITE
				+ "Add materials to your filter.");
		player.sendMessage(ChatColor.YELLOW + " /filter remove <material>" + ChatColor.GOLD + " » " + ChatColor.WHITE
				+ "Remove materials from your filter.");
		player.sendMessage(colour("&8&m-----------------------"));
	}

	public static String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	@EventHandler
	public void onPickUp(PlayerPickupItemEvent event) {
		event.getPlayer();
		Material type = event.getItem().getItemStack().getType();
		// Bukkit.broadcastMessage("" + user.getFilter(type).toString());
		if (!inFilter()) {
			return;
		}
		if (inFilter() && (getFilter() == type)) {
			event.setCancelled(true);
		}
	}

	private Material getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean inFilter() {
		// TODO Auto-generated method stub
		return false;

	}

}
