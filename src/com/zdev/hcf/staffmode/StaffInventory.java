package com.zdev.hcf.staffmode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import com.zdev.hcf.Base;
import com.zdev.hcf.util.TimeUtils;

import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;

public class StaffInventory {
	private final static Base plugin = Base.getPlugin();

	public static String translate(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}

	public static List<String> translateFromArray(List<String> text) {
		List<String> messages = new ArrayList<String>();
		for (String string : text) {
			messages.add(translate(string));
		}
		return messages;
	}

	public static void inspector(Player player, Player target) {
		Inventory inventory = Bukkit.getServer().createInventory(null, 45,
				ChatColor.translateAlternateColorCodes('&', "&eInspecting: " + target.getName()));
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				PlayerInventory playerInventory = target.getInventory();

				ItemStack cookedBeef = new ItemStack(Material.COOKED_BEEF, target.getFoodLevel());
				ItemMeta cookedBeefMeta = cookedBeef.getItemMeta();
				cookedBeefMeta.setDisplayName(translate("&eHunger"));
				cookedBeef.setItemMeta(cookedBeefMeta);

				ItemStack brewingStand = new ItemStack(Material.BREWING_STAND_ITEM,
						target.getPlayer().getActivePotionEffects().size());
				ItemMeta brewingStandMeta = brewingStand.getItemMeta();
				brewingStandMeta.setDisplayName(translate("&eActive Effects"));
				ArrayList<String> brewingStandLore = new ArrayList<String>();
				for (PotionEffect potionEffect : target.getPlayer().getActivePotionEffects()) {
					String effectName = potionEffect.getType().getName();
					int effectLevel = potionEffect.getAmplifier();
					effectLevel++;
					brewingStandLore.add(translate("&e" + WordUtils.capitalizeFully(effectName).replace("_", " ") + " "
							+ effectLevel + "&7: &c"
							+ TimeUtils.IntegerCountdown.setFormat(Integer.valueOf(potionEffect.getDuration() / 20))));
				}
				brewingStandMeta.setLore(brewingStandLore);
				brewingStand.setItemMeta(brewingStandMeta);

				ItemStack compass = new ItemStack(Material.COMPASS, 1);
				ItemMeta compassMeta = compass.getItemMeta();
				compassMeta.setDisplayName(translate("&ePlayer Location"));
				compassMeta.setLore(
						translateFromArray(Arrays.asList(new String[] { "&eWorld&7: &e" + player.getWorld().getName(),
								"&eCoords", "  &eX&7: &c" + target.getLocation().getBlockX(),
								"  &eY&7: &c" + target.getLocation().getBlockY(),
								"  &eZ&7: &c" + target.getLocation().getBlockZ() })));
				compass.setItemMeta(compassMeta);

				inventory.setContents(playerInventory.getContents());
				inventory.setItem(36, playerInventory.getHelmet());
				inventory.setItem(37, playerInventory.getChestplate());
				inventory.setItem(38, playerInventory.getLeggings());
				inventory.setItem(39, playerInventory.getBoots());
				inventory.setItem(40, playerInventory.getItemInHand());
				inventory.setItem(42, cookedBeef);
				inventory.setItem(43, brewingStand);
				inventory.setItem(44, compass);

			}
		}, 0, 5);
		player.openInventory(inventory);
	}
}
