package com.zdev.hcf.sotw;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.zdev.hcf.Base;
import com.zdev.hcf.ConfigurationService;

public class SotwListener implements Listener {

	private final Base plugin;

	public SotwListener(Base plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void EntityDamageEvent(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			final Player player = (Player) e.getEntity();
			final Player oponent = (Player) e.getDamager();
			if (this.plugin.getSotwTimer().getSotwRunnable() != null
					&& SotwCommand.enabled.contains(oponent.getUniqueId())
					&& !SotwCommand.enabled.contains(player.getUniqueId())) {
				oponent.sendMessage(ChatColor.GRAY + "§m----------------------------------");
				oponent.sendMessage(
						"§7You are not permitted to hit this player, they do not have their §a§lSOTW §7paused.");
				oponent.sendMessage(ChatColor.GRAY + "§m----------------------------------");
				e.setCancelled(true);
			} else if (this.plugin.getSotwTimer().getSotwRunnable() != null
					&& !SotwCommand.enabled.contains(oponent.getUniqueId())
					&& SotwCommand.enabled.contains(player.getUniqueId())) {
				oponent.sendMessage(ChatColor.GRAY + "§m----------------------------------");
				oponent.sendMessage("§7You are not permitted to hit §a§l" + player.getName() + "§e.");
				oponent.sendMessage(ChatColor.GRAY + "§m----------------------------------");
				e.setCancelled(true);
			} else if (this.plugin.getSotwTimer().getSotwRunnable() != null
					&& !SotwCommand.enabled.contains(oponent.getUniqueId())
					&& !SotwCommand.enabled.contains(player.getUniqueId())) {
				oponent.sendMessage(ChatColor.GRAY + "§m----------------------------------");
				oponent.sendMessage(
						"§7You cannot hit players whilst sotw is active if you would like to execute §a§l/sotw enable§e.");
				oponent.sendMessage(ChatColor.GRAY + "§m----------------------------------");
				e.setCancelled(true);
			}

		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player && this.plugin.getSotwTimer().getSotwRunnable() != null) {
			final Player player = (Player) event.getEntity();
			if (SotwCommand.enabled.contains(player.getUniqueId())) {
				event.setCancelled(false);
				return;
			}
			if (event.getCause() != EntityDamageEvent.DamageCause.SUICIDE
					&& this.plugin.getSotwTimer().getSotwRunnable() != null) {
				event.setCancelled(true);
			}
		}
	}
}
