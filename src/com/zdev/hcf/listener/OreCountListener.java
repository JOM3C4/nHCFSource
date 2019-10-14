package com.zdev.hcf.listener;

import com.google.common.collect.ImmutableMultimap;
import com.zdev.hcf.Base;

import lombok.RequiredArgsConstructor;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OreCountListener implements Listener {

	public static final ImmutableMultimap<Material, ChatColor> ORES = ImmutableMultimap.<Material, ChatColor>builder()
			.put(Material.EMERALD_ORE, ChatColor.GREEN).put(Material.DIAMOND_ORE, ChatColor.AQUA)
			.put(Material.REDSTONE_ORE, ChatColor.RED).put(Material.GLOWING_REDSTONE_ORE, ChatColor.RED)
			.put(Material.GOLD_ORE, ChatColor.GOLD).put(Material.IRON_ORE, ChatColor.GRAY)
			.put(Material.LAPIS_ORE, ChatColor.BLUE).put(Material.COAL_ORE, ChatColor.DARK_GRAY).build();

	private Base plugin;

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		if (ORES.containsKey(event.getBlock().getType()) && player.getItemInHand() != null
				&& !(player.getItemInHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH))) {
			Material type = event.getBlock().getType();
			if (type == Material.GLOWING_REDSTONE_ORE)
				type = Material.REDSTONE_ORE;

			plugin.getUserManager().getUser(player.getUniqueId()).incrementOre(type);
		}
	}
}
