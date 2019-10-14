package com.zdev.hcf.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class ItemUtil {

	public static final ItemStack FILLER = new ItemBuilder(Material.STAINED_GLASS_PANE)
			.displayName(ChatColor.WHITE.toString()).build();

}
