package com.zdev.hcf.timer.type;

import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.zdev.hcf.Base;
import com.zdev.hcf.timer.PlayerTimer;

public class GappleTimer extends PlayerTimer implements Listener {
	public GappleTimer(JavaPlugin plugin) {
		super("Gopple", TimeUnit.HOURS.toMillis(1L));
	}

	public String getScoreboardPrefix() {
		return ChatColor.GOLD.toString() + ChatColor.BOLD;
	}

	@EventHandler
	public void onPlayerConsume(PlayerItemConsumeEvent event) {
		ItemStack stack = event.getItem();
		if ((stack != null) && (stack.getType() == Material.GOLDEN_APPLE) && (stack.getDurability() == 1)) {
			Player player = event.getPlayer();
			if (this.getRemaining(player) <= 0) {
				this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, false);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&c\u2588\u2588\u2588\u2588\u2588&c\u2588\u2588\u2588"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&c\u2588\u2588\u2588&e\u2588\u2588&c\u2588\u2588\u2588"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&c\u2588\u2588\u2588&e\u2588&c\u2588\u2588\u2588\u2588 &6&l " + this.name + ": "));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&c\u2588\u2588&6\u2588\u2588\u2588\u2588&c\u2588\u2588 &7  Consumed"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&c\u2588&6\u2588\u2588&f\u2588&6\u2588&6\u2588\u2588&c\u2588"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&c\u2588&6\u2588&f\u2588&6\u2588&6\u2588&6\u2588\u2588&c\u2588 &6 Cooldown Remaining:"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&c\u2588&6\u2588\u2588&6\u2588&6\u2588&6\u2588\u2588&c\u2588 &7  "
								+ Base.getRemaining(this.defaultCooldown, true, true)));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&c\u2588&6\u2588\u2588&6\u2588&6\u2588&6\u2588\u2588&c\u2588"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&c\u2588\u2588&6\u2588\u2588\u2588\u2588&c\u2588\u2588"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&c\u2588\u2588\u2588\u2588\u2588&c\u2588\u2588\u2588"));
			} else {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "You still have a " + getDisplayName() + ChatColor.RED
						+ " cooldown for another " + ChatColor.BOLD
						+ Base.getRemaining(getRemaining(player), true, false) + ChatColor.RED + '.');
			}
		}
	}

}
