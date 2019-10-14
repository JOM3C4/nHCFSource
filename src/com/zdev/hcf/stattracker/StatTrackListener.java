package com.zdev.hcf.stattracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StatTrackListener implements Listener {
	private static final String FORGED_LORE_PREFIX;
	private static final String BLANK_SPACE;
	private static final int MAX_KILL_STATS;

	static {
		FORGED_LORE_PREFIX = ChatColor.YELLOW + "Forged by ";
		BLANK_SPACE = " ";
		MAX_KILL_STATS = 3;
	}

	@EventHandler
	public void onPlayerDeath(final PlayerDeathEvent event) {
		final Player player = event.getEntity();
		final Player killer = player.getKiller();
		if (killer != null) {
			final ItemStack stack = killer.getItemInHand();
			if (stack != null && EnchantmentTarget.WEAPON.includes(stack)) {
				this.addDeathLore(stack, player, killer);
			}
		}
	}

	private void addDeathLore(final ItemStack stack, final Player player, final Player killer) {
		final ItemMeta meta = stack.getItemMeta();
		final List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>(2);
		if (lore.isEmpty() || !lore.get(0).equals(StatTrackListener.BLANK_SPACE)) {
			lore.add(0, StatTrackListener.BLANK_SPACE);
		}
		final Date now = new Date();
		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		final int index = (lore.size() <= 1 || !lore.get(1).startsWith(StatTrackListener.FORGED_LORE_PREFIX)) ? 1 : 2;
		lore.add(index,
				ChatColor.GOLD + player.getName() + ChatColor.YELLOW + ' '
						+ ((killer != null)
								? ("slain by " + ChatColor.GOLD + killer.getName() + StatTrackListener.BLANK_SPACE
										+ ChatColor.GRAY + format.format(now))
								: "died"));
		meta.setLore((List) lore.subList(0, Math.min(StatTrackListener.MAX_KILL_STATS + 2, lore.size())));
		stack.setItemMeta(meta);
	}

}
