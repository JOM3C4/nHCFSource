package com.zdev.hcf.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
import com.zdev.hcf.faction.type.ClaimableFaction;
import com.zdev.hcf.faction.type.Faction;

public class PearlGlitchListener implements Listener {
	private final ImmutableSet<Material> blockedPearlTypes;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PearlGlitchListener(final Base plugin) {
		this.blockedPearlTypes = (ImmutableSet<Material>) Sets.immutableEnumSet((Enum) Material.THIN_GLASS,
				(Enum[]) new Material[] { Material.IRON_FENCE, Material.FENCE, Material.NETHER_FENCE });
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPearlClip(final PlayerTeleportEvent event) {
		if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
			final Location to = event.getTo();
			final Block block = to.getBlock();
			final Material type = block.getType();
			if (this.blockedPearlTypes.contains((Object) type)) {
				final Block above = block.getRelative(BlockFace.UP);
				final Material aboveType = above.getType();
				if (this.blockedPearlTypes.contains((Object) aboveType) || aboveType.isSolid()) {
					final Player player = event.getPlayer();
					player.sendMessage(ChatColor.YELLOW + "Pearl glitching detected, your pearl has been refunded");
					event.setCancelled(true);
				} else {
					to.add(0.0, 1.0, 0.0);
					event.setTo(to);
				}
			}
		}
	}
}
