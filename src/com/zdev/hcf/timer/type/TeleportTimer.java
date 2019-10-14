package com.zdev.hcf.timer.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.zdev.hcf.Base;
import com.zdev.hcf.faction.FactionManager;
import com.zdev.hcf.faction.type.Faction;
import com.zdev.hcf.timer.PlayerTimer;
import com.zdev.hcf.timer.TimerCooldown;

public class TeleportTimer extends PlayerTimer implements Listener {

	private final Map<UUID, Location> destinationMap = new HashMap<>();
	private final Base plugin;

	public TeleportTimer(Base plugin) {
		super("Teleport", TimeUnit.SECONDS.toMillis(10L), false);
		this.plugin = plugin;
	}

	public Location getDestination(Player player) {
		return destinationMap.get(player.getUniqueId());
	}

	@Override
	public String getScoreboardPrefix() {
		return ChatColor.DARK_AQUA.toString() + ChatColor.BOLD;
	}

	@Override
	public TimerCooldown clearCooldown(UUID uuid) {
		TimerCooldown runnable = super.clearCooldown(uuid);
		if (runnable != null) {
			destinationMap.remove(uuid);
			return runnable;
		}

		return null;
	}

	@SuppressWarnings("deprecation")
	public int getNearbyEnemies(Player player, int distance) {
		FactionManager factionManager = plugin.getFactionManager();
		Faction playerFaction = factionManager.getPlayerFaction(player.getUniqueId());
		int count = 0;

		Collection<Entity> nearby = player.getNearbyEntities(distance, distance, distance);
		for (Entity entity : nearby) {
			if (entity instanceof Player) {
				Player target = (Player) entity;

				if (!target.canSee(player) || !player.canSee(target)) {
					continue;
				}

				if (playerFaction == null || factionManager.getPlayerFaction(target) != playerFaction) {
					count++;
				}
			}
		}

		return count;
	}

	public boolean teleport(Player player, Location location, long millis, String warmupMessage,
			PlayerTeleportEvent.TeleportCause cause) {
		cancelTeleport(player, null); // cancels any previous teleport for the player.

		boolean result;
		if (millis <= 0) { // if there is no delay, just instantly teleport.
			result = player.teleport(location, cause);
			clearCooldown(player.getUniqueId());
		} else {
			UUID uuid = player.getUniqueId();
			player.sendMessage(warmupMessage);
			destinationMap.put(uuid, location.clone());
			setCooldown(player, uuid, millis, true, null);
			result = true;
		}

		return result;
	}

	public void cancelTeleport(Player player, String reason) {
		UUID uuid = player.getUniqueId();
		if (getRemaining(uuid) > 0L) {
			clearCooldown(uuid);
			if (reason != null && !reason.isEmpty()) {
				player.sendMessage(reason);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event) {
		Location from = event.getFrom();
		Location to = event.getTo();
		// Player didn't move a block.
		if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY()
				&& from.getBlockZ() == to.getBlockZ()) {
			return;
		}

		cancelTeleport(event.getPlayer(), ChatColor.YELLOW + "You moved a block, therefore cancelling your teleport.");
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			cancelTeleport((Player) entity, ChatColor.YELLOW + "You took damage, therefore cancelling your teleport.");
		}
	}

	@Override
	public void onExpire(UUID userUUID) {
		Player player = Bukkit.getPlayer(userUUID);
		if (player == null)
			return;

		Location destination = this.destinationMap.remove(userUUID);
		if (destination != null) {
			destination.getChunk(); // pre-load the chunk before teleport.
			player.teleport(destination, PlayerTeleportEvent.TeleportCause.COMMAND);
		}
	}
}
