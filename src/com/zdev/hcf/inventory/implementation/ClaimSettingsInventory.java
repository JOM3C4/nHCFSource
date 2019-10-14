package com.zdev.hcf.inventory.implementation;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.zdev.hcf.Base;
import com.zdev.hcf.faction.claim.Claim;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.inventory.CustomInventory;
import com.zdev.hcf.inventory.CustomInventoryListener;
import com.zdev.hcf.inventory.implementation.runnable.BlockChangeRunnable;
import com.zdev.hcf.inventory.implementation.runnable.BlockChangeRunnable.BlockOperation;
import com.zdev.hcf.util.ItemBuilder;
import com.zdev.hcf.util.ItemUtil;
import com.zdev.hcf.util.Message;

import net.md_5.bungee.api.ChatColor;

public class ClaimSettingsInventory extends CustomInventory<Base> {

	private static final ItemStack COBBLE_BOX = new ItemBuilder(Material.COBBLE_WALL)
			.displayName(ChatColor.GOLD + "Generate Cobblestone Box").build();
	private static final ItemStack STONE_BOX = new ItemBuilder(Material.STONE)
			.displayName(ChatColor.GOLD + "Generate Stone Box").build();
	private static final ItemStack DIRT_BOX = new ItemBuilder(Material.DIRT)
			.displayName(ChatColor.GOLD + "Generate Dirt Box").build();
	private static final ItemStack NETHERACK = new ItemBuilder(Material.NETHERRACK)
			.displayName(ChatColor.GOLD + "Generate Netherack Box").build();

	public ClaimSettingsInventory(Base plugin) {
		super(plugin, ChatColor.GOLD + "Claim Settings", 27);
	}

	@Override
	public void initialize() {
		for (int i = 0; i < size; i++)
			this.inventory.setItem(i, ItemUtil.FILLER);
		this.inventory.setItem(10, COBBLE_BOX);
		this.inventory.setItem(12, STONE_BOX);
		this.inventory.setItem(14, DIRT_BOX);
		this.inventory.setItem(16, NETHERACK);
	}

	@Override
	public void registerListeners() {
		this.listeners.add(new CustomInventoryListener() {

			@Override
			public void onOpen(InventoryOpenEvent event) {

			}

			@Override
			public void onDrag(InventoryDragEvent event) {
				event.setCancelled(true);
			}

			@Override
			public void onClose(InventoryCloseEvent event) {

			}

			@Override
			public void onClick(InventoryClickEvent event) {
				ItemStack item = event.getCurrentItem();
				Player player = (Player) event.getWhoClicked();

				// cobble, stone, dirt

				if (item.getType() == Material.COBBLE_WALL) {
					if (!player.hasPermission("hcf.command.faction.build.use.cobble")) {
						player.sendMessage(Message.NO_PERMISSION.toString());
						return;
					}

					PlayerFaction faction = plugin.getFactionManager().getPlayerFaction(player.getUniqueId());

					if (faction == null) {
						player.sendMessage(ChatColor.RED + "You need to be in a faction to do this!");

						player.closeInventory();
						event.setCancelled(true);
						return;
					}

					Set<Claim> claims = faction.getClaims();

					if (claims.isEmpty()) {
						player.sendMessage(ChatColor.RED + "No claim found!");

						player.closeInventory();
						event.setCancelled(true);
						return;
					}

					Claim claim = claims.stream().findFirst().orElse(null);

					if (claim.getWidth() > 50) {
						player.sendMessage(ChatColor.RED + "Your claim is to wide (wider than 50 blocks)");
						return;
					}

					Location min = claim.getMinimumPoint();
					Location max = claim.getMaximumPoint();
					Location center = claim.getCenter();

					int minY = center.getWorld().getHighestBlockAt(center).getY();

					new BlockChangeRunnable(BlockOperation.X, Material.COBBLESTONE, minY, min, max, 1)
							.runTaskTimer(plugin, 0, 1);
					new BlockChangeRunnable(BlockOperation.Z, Material.COBBLESTONE, minY, min, max, 1)
							.runTaskTimer(plugin, 0, 1);
				} else if (item.getType() == Material.STONE) {
					if (!player.hasPermission("hcf.command.faction.build.use.stone")) {
						player.sendMessage(Message.NO_PERMISSION.toString());
						return;
					}

					PlayerFaction faction = plugin.getFactionManager().getPlayerFaction(player.getUniqueId());

					if (faction == null) {
						player.sendMessage(ChatColor.RED + "You need to be in a faction to do this!");

						player.closeInventory();
						event.setCancelled(true);
						return;
					}

					Set<Claim> claims = faction.getClaims();

					if (claims.isEmpty()) {
						player.sendMessage(ChatColor.RED + "No claim found!");

						player.closeInventory();
						event.setCancelled(true);
						return;
					}

					Claim claim = claims.stream().findFirst().orElse(null);

					if (claim.getWidth() > 50) {
						player.sendMessage(ChatColor.RED + "Your claim is to wide (wider than 50 blocks)");

						player.closeInventory();
						event.setCancelled(true);
						return;
					}

					Location min = claim.getMinimumPoint();
					Location max = claim.getMaximumPoint();
					Location center = claim.getCenter();

					int minY = center.getWorld().getHighestBlockAt(center).getY();

					new BlockChangeRunnable(BlockOperation.X, Material.STONE, minY, min, max, 1).runTaskTimer(plugin, 0,
							1);
					new BlockChangeRunnable(BlockOperation.Z, Material.STONE, minY, min, max, 1).runTaskTimer(plugin, 0,
							1);
				} else if (item.getType() == Material.DIRT) {
					if (!player.hasPermission("hcf.command.faction.build.use.dirt")) {
						player.sendMessage(Message.NO_PERMISSION.toString());
						return;
					}
					PlayerFaction faction = plugin.getFactionManager().getPlayerFaction(player.getUniqueId());

					if (faction == null) {
						player.sendMessage(ChatColor.RED + "You need to be in a faction to do this!");

						player.closeInventory();
						event.setCancelled(true);
						return;
					}

					Set<Claim> claims = faction.getClaims();

					if (claims.isEmpty()) {
						player.sendMessage(ChatColor.RED + "No claim found!");

						player.closeInventory();
						event.setCancelled(true);
						return;
					}

					Claim claim = claims.stream().findFirst().orElse(null);

					if (claim.getWidth() > 50) {
						player.sendMessage(ChatColor.RED + "Your claim is to wide (wider than 50 blocks)");

						player.closeInventory();
						event.setCancelled(true);
						return;
					}

					Location min = claim.getMinimumPoint();
					Location max = claim.getMaximumPoint();
					Location center = claim.getCenter();

					int minY = center.getWorld().getHighestBlockAt(center).getY();

					new BlockChangeRunnable(BlockOperation.X, Material.DIRT, minY, min, max, 1).runTaskTimer(plugin, 0,
							1);
					new BlockChangeRunnable(BlockOperation.Z, Material.DIRT, minY, min, max, 1).runTaskTimer(plugin, 0,
							1);
				} else if (item.getType() == Material.DIRT) {
					if (!player.hasPermission("hcf.command.faction.build.use.dirt")) {
						player.sendMessage(Message.NO_PERMISSION.toString());
						return;
					}

					PlayerFaction faction = plugin.getFactionManager().getPlayerFaction(player.getUniqueId());

					if (faction == null) {
						player.sendMessage(ChatColor.RED + "You need to be in a faction to do this!");

						player.closeInventory();
						event.setCancelled(true);
						return;
					}

					Set<Claim> claims = faction.getClaims();

					if (claims.isEmpty()) {
						player.sendMessage(ChatColor.RED + "No claim found!");

						player.closeInventory();
						event.setCancelled(true);
						return;
					}

					Claim claim = claims.stream().findFirst().orElse(null);

					if (claim.getWidth() > 50) {
						player.sendMessage(ChatColor.RED + "Your claim is to wide (wider than 50 blocks)");

						player.closeInventory();
						event.setCancelled(true);
						return;
					}

					Location min = claim.getMinimumPoint();
					Location max = claim.getMaximumPoint();
					Location center = claim.getCenter();

					int minY = center.getWorld().getHighestBlockAt(center).getY();

					new BlockChangeRunnable(BlockOperation.X, Material.DIRT, minY, min, max, 1).runTaskTimer(plugin, 0,
							1);
					new BlockChangeRunnable(BlockOperation.Z, Material.DIRT, minY, min, max, 1).runTaskTimer(plugin, 0,
							1);
				} else if (item.getType() == Material.NETHERRACK) {
					if (!player.hasPermission("hcf.command.faction.build.use.netherack")) {
						player.sendMessage(Message.NO_PERMISSION.toString());
						return;
					}

					PlayerFaction faction = plugin.getFactionManager().getPlayerFaction(player.getUniqueId());

					if (faction == null) {
						player.sendMessage(ChatColor.RED + "You need to be in a faction to do this!");

						player.closeInventory();
						event.setCancelled(true);
						return;
					}

					Set<Claim> claims = faction.getClaims();

					if (claims.isEmpty()) {
						player.sendMessage(ChatColor.RED + "No claim found!");
						return;
					}

					Claim claim = claims.stream().findFirst().orElse(null);

					if (claim.getWidth() > 50) {
						player.sendMessage(ChatColor.RED + "Your claim is to wide (wider than 50 blocks)");

						player.closeInventory();
						event.setCancelled(true);
						return;
					}

					Location min = claim.getMinimumPoint();
					Location max = claim.getMaximumPoint();
					Location center = claim.getCenter();

					int minY = center.getWorld().getHighestBlockAt(center).getY();

					new BlockChangeRunnable(BlockOperation.X, Material.NETHERRACK, minY, min, max, 1)
							.runTaskTimer(plugin, 0, 1);
					new BlockChangeRunnable(BlockOperation.Z, Material.NETHERRACK, minY, min, max, 1)
							.runTaskTimer(plugin, 0, 1);
				}

				player.closeInventory();
				event.setCancelled(true);
			}

		});
	}

	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

}
