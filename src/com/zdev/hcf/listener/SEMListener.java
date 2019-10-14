package com.zdev.hcf.listener;

import lombok.RequiredArgsConstructor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.zdev.hcf.Base;

import java.util.concurrent.TimeUnit;

public class SEMListener implements Listener {

	private final static long TPS_CLEAR_DELAY = TimeUnit.SECONDS.toMillis(5L);
	private Base plugin;

	private long lastTPSRun = 0L;

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (shouldClear()) {
			for (Entity entity : plugin.getServer().getWorlds().get(0).getEntities()) {
				if (entity instanceof Player || entity instanceof Projectile || entity instanceof ItemFrame
						|| entity instanceof Pig || entity instanceof Minecart || entity instanceof Chicken) {
					continue;
				}

				if (entity instanceof Item) {
					Item item = (Item) entity;

					if (item.getItemStack() != null && item.getItemStack().getType().name().contains("DIAMOND")
							&& !item.getItemStack().getEnchantments().isEmpty()) {
						continue;
					}
				}

				entity.remove();
			}
		}
	}

	private boolean shouldClear() {
		if (!(plugin.getSotwTimer().getSotwRunnable() == null) && plugin.getServer().spigot().getTPS()[0] <= 18
				&& lastTPSRun + TPS_CLEAR_DELAY < System.currentTimeMillis()) {
			lastTPSRun = System.currentTimeMillis();
			return true;
		}

		int onlinePlayers = Bukkit.getOnlinePlayers().length;
		int entities = plugin.getServer().getWorlds().get(0).getEntities().size() - onlinePlayers;

		return onlinePlayers >= 200 && entities >= 3500 || onlinePlayers >= 100 && entities >= 4000;
	}

}
