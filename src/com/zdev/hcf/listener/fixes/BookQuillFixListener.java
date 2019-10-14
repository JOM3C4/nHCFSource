package com.zdev.hcf.listener.fixes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import com.zdev.hcf.Base;

public class BookQuillFixListener implements Listener {

	public BookQuillFixListener(Base plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void craftBookEvent(PrepareItemCraftEvent e) {
		Material itemType = e.getRecipe().getResult().getType();
		if (itemType == Material.BOOK_AND_QUILL) {
			e.getInventory().setResult(new ItemStack(Material.AIR));
			for (HumanEntity he : e.getViewers()) {
				if (he instanceof Player) {
					((Player) he).sendMessage(ChatColor.RED + "This item is disabled.");
				}
			}
		}
	}
}