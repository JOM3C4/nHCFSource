package com.zdev.hcf.specialitems;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Bukkit;
import java.util.List;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class GrappleCommand implements CommandExecutor {
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] argument) {
		if (!(sender instanceof Player)) {
			return true;
		}
		final Player player = (Player) sender;
		final ItemStack scout = new ItemStack(Material.FISHING_ROD);
		final ItemMeta scoutMeta = scout.getItemMeta();
		scoutMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6" + GrappleListener.grappleName));
		final List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7" + GrappleListener.grappleLore));
		scoutMeta.setLore((List) lore);
		scout.setItemMeta(scoutMeta);
		if (player.hasPermission("hcf.command.grapple")) {
			if (argument.length == 0) {
				player.getInventory().addItem(new ItemStack[] { scout });
				sender.sendMessage("§aGrapple added in your inventory");
			}
			if (argument.length == 1) {
				final String name = argument[0];
				try {
					final Player target = Bukkit.getPlayer(name);
					target.getInventory().addItem(new ItemStack[] { scout });
					target.sendMessage("§aGrapple added in your inventory");
					sender.sendMessage("§aGrapple added in " + name + " inventory");
				} catch (NullPointerException e) {
					sender.sendMessage("§6Player '&§" + name + "§6' not found!");
				}
			}
		}
		return false;
	}
}
