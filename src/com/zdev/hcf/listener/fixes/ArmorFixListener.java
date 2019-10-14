package com.zdev.hcf.listener.fixes;

import org.bukkit.*;
import org.bukkit.event.player.*;
import java.util.concurrent.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;
import java.util.*;

public class ArmorFixListener implements Listener {
	private static List<Material> ALLOWED;

	@EventHandler
	public void onItemDamage(final PlayerItemDamageEvent event) {
		final ItemStack stack = event.getItem();
		if (stack != null && ArmorFixListener.ALLOWED.contains(stack.getType())
				&& ThreadLocalRandom.current().nextInt(3) != 0) {
			event.setCancelled(true);
		}
	}

	static {
		ArmorFixListener.ALLOWED = Arrays.asList(Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS,
				Material.GOLD_BOOTS, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS,
				Material.IRON_BOOTS, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE,
				Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS, Material.LEATHER_HELMET,
				Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.DIAMOND_HELMET,
				Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS);
	}
}
