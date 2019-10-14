package com.zdev.hcf.signs;

import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.block.*;
import org.bukkit.event.block.*;
import org.bukkit.event.*;

public class KitSignListener implements Listener {
	@EventHandler
	public void onclicksigncrowbarbuy(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			final BlockState state = e.getClickedBlock().getState();
			if (state instanceof Sign) {
				final Sign s = (Sign) state;
				if (s.getLine(0).equals(ChatColor.translateAlternateColorCodes('&', "&b[Kit]"))
						&& s.getLine(1).equals("Diamond")) {
					Kit.giveDiamondKit(p);
					p.sendMessage(ChatColor.GREEN + "You have received Diamond kit.");
				} else if (s.getLine(0).equals(ChatColor.translateAlternateColorCodes('&', "&b[Kit]"))
						&& s.getLine(1).equals("Bard")) {
					Kit.giveBardKit(p);
					p.sendMessage(ChatColor.GREEN + "You have received Bard kit.");
				} else if (s.getLine(0).equals(ChatColor.translateAlternateColorCodes('&', "&b[Kit]"))
						&& s.getLine(1).equals("Archer")) {
					Kit.giveArcherKit(p);
					p.sendMessage(ChatColor.GREEN + "You have received Archer kit.");
				} else if (s.getLine(0).equals(ChatColor.translateAlternateColorCodes('&', "&b[Kit]"))
						&& s.getLine(1).equals("Rogue")) {
					Kit.giveRogueKit(p);
					p.sendMessage(ChatColor.GREEN + "You have received Rogue kit.");
				} else if (s.getLine(0).equals(ChatColor.translateAlternateColorCodes('&', "&b[Kit]"))
						&& s.getLine(1).equals("Builder")) {
					Kit.giveBuildKit(p);
					p.sendMessage(ChatColor.GREEN + "You have received Builder kit.");
				} else if (s.getLine(0).equals(ChatColor.translateAlternateColorCodes('&', "&b[Kit]"))
						&& s.getLine(1).equals("Miner")) {
					Kit.giveMinerKit(p);
					p.sendMessage(ChatColor.GREEN + "You have received Miner kit.");
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onSignCreate(final SignChangeEvent event) {
		final Player player = event.getPlayer();
		if (player != null && player.hasPermission("sign.colour")) {
			final String[] lines = event.getLines();
			for (int i = 0; i < lines.length; ++i) {
				if (!player.hasPermission("base.sign.admin")
						&& (event.getLine(i).contains(ChatColor.translateAlternateColorCodes('&', "Sell"))
								|| event.getLine(i).contains("Buy") || event.getLine(i).contains("Kit"))) {
					player.sendMessage(ChatColor.RED + "You have used a sign that you're not allowed.");
					event.setCancelled(true);
				}
				event.setLine(i, ChatColor.translateAlternateColorCodes('&', lines[i]));
			}
		}
	}
}
