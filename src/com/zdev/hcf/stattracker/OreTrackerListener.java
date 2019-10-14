package com.zdev.hcf.stattracker;

import com.google.common.collect.Lists;
import com.zdev.hcf.listener.OreCountListener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static org.bukkit.Material.COAL_ORE;
import static org.bukkit.Material.DIAMOND_ORE;
import static org.bukkit.Material.GOLD_ORE;
import static org.bukkit.Material.IRON_ORE;
import static org.bukkit.Material.LAPIS_ORE;
import static org.bukkit.Material.REDSTONE_ORE;

/**
 * Created by Julio on 7/14/2016.
 */
public class OreTrackerListener implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Material type;

		if (!OreCountListener.ORES.containsKey(event.getBlock().getType())) {
			return;
		}

		type = event.getBlock().getType();

		if (type.equals(Material.GLOWING_REDSTONE_ORE)) {
			type = Material.REDSTONE_ORE;
		}

		if (player.getItemInHand() == null) {
			return;
		}

		ItemStack item = player.getItemInHand();

		if (!item.getType().name().toUpperCase().contains("PICKAXE")
				|| item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
			return;
		}

		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.hasLore() ? meta.getLore() : Lists.newArrayList();

		int index = getIndex(type);

		if (index == -1) {
			return;
		}

		if (!(lore.size() >= 5)) {
			for (int i = 0; i < 6; i += 1) {
				Material adding = getType(i);
				ChatColor color = OreCountListener.ORES.get(adding).asList().get(0);
				lore.add(color + "This pickaxe has mined " + ChatColor.BOLD + color
						+ (adding.equals(type) ? "1 " : "0 ") + adding.name().toLowerCase().replace("_", " "));
			}
		} else {
			try {
				ChatColor color = OreCountListener.ORES.get(type).asList().get(0);
				Integer count = Integer.valueOf(ChatColor.stripColor(lore.get(index)).split(" ")[4]) + 1;
				lore.set(index, color + "This pickaxe has mined " + ChatColor.BOLD + color + count + ' '
						+ type.name().toLowerCase().replace("_", " "));
			} catch (Exception ignored) {
			}
		}

		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	private int getIndex(Material type) {
		switch (type) {
		case DIAMOND_ORE:
			return 0;
		case GOLD_ORE:
			return 1;
		case IRON_ORE:
			return 2;
		case REDSTONE_ORE:
			return 3;
		case LAPIS_ORE:
			return 4;
		case COAL_ORE:
			return 5;
		}

		return -1;
	}

	private Material getType(int index) {
		switch (index) {
		case 0:
			return DIAMOND_ORE;
		case 1:
			return GOLD_ORE;
		case 2:
			return IRON_ORE;
		case 3:
			return REDSTONE_ORE;
		case 4:
			return LAPIS_ORE;
		case 5:
			return COAL_ORE;
		}

		return Material.AIR;
	}
}
