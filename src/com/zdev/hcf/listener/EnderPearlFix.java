package com.zdev.hcf.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.InventoryHolder;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.zdev.hcf.Base;

import net.md_5.bungee.api.ChatColor;

public class EnderPearlFix implements Listener {
	private final ImmutableSet<Material> blockedPearlTypes;

	public EnderPearlFix(Base plugin) {
		this.blockedPearlTypes = Sets.immutableEnumSet(Material.THIN_GLASS,
				new Material[] { Material.IRON_FENCE, Material.FENCE, Material.NETHER_FENCE });
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
			Location location = event.getTo();

			location.setX(location.getBlockX() + 0.5D);
			location.setY(location.getBlockY());
			location.setZ(location.getBlockZ() + 0.5D);

			event.setTo(location);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPearlClip(PlayerTeleportEvent event) {
		if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
			Location to = event.getTo();
			if (this.blockedPearlTypes.contains(to.getBlock().getType())) {
				Player player = event.getPlayer();
				player.sendMessage(ChatColor.RED + "Invalid Pearl! Please try again.");
				event.setCancelled(true);
				return;
			}
			to.setX(to.getBlockX() + 0.5D);
			to.setZ(to.getBlockZ() + 0.5D);
			event.setTo(to);
		}
	}
}