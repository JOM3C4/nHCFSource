package com.zdev.hcf.deathban;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.zdev.hcf.Base;
import com.zdev.hcf.ConfigurationService;
import com.zdev.hcf.user.FactionUser;
import com.zdev.hcf.util.DelayedMessageRunnable;
import com.zdev.hcf.util.DurationFormatter;

import net.minecraft.util.gnu.trove.map.TObjectIntMap;
import net.minecraft.util.gnu.trove.map.TObjectLongMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap;

public class DeathbanListener implements Listener {

	private static final long RESPAWN_KICK_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(10L);
	private static final long RESPAWN_KICK_DELAY_TICKS = RESPAWN_KICK_DELAY_MILLIS / 50L;
	private static final long LIFE_USE_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(30L);
	private static final String LIFE_USE_DELAY_WORDS = DurationFormatUtils
			.formatDurationWords(DeathbanListener.LIFE_USE_DELAY_MILLIS, true, true);
	private static final String DEATH_BAN_BYPASS_PERMISSION = "hcf.deathban.bypass";

	private final TObjectIntMap<UUID> respawnTickTasks = new TObjectIntHashMap<>();
	private final TObjectLongMap<UUID> lastAttemptedJoinMap = new TObjectLongHashMap<>();
	private final Base plugin;

	public DeathbanListener(Base plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission(DeathbanListener.DEATH_BAN_BYPASS_PERMISSION) || ConfigurationService.KIT_MAP) {
			return;
		}

		FactionUser user = this.plugin.getUserManager().getUser(player.getUniqueId());
		Deathban deathban = user.getDeathban();

		if (deathban == null || !deathban.isActive())
			return;

		if (this.plugin.getEotwHandler().isEndOfTheWorld()) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED
					+ "Deathbanned for the entirety of the map due to EOTW.\nVisit shop.hcrealms.us to view SOTW info.");
			return;
		}

		UUID uuid = player.getUniqueId();
		int lives = this.plugin.getDeathbanManager().getLives(uuid);

		String formattedRemaining = DurationFormatter.getRemaining(deathban.getRemaining(), true, false);

		if (lives <= 0) { // If the user has no lives, inform that they need some.
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "You are still deathbanned for "
					+ formattedRemaining + ": " + ChatColor.YELLOW + deathban.getReason() + ChatColor.RED + ".");

			System.out.println(
					event.getPlayer().getName() + " attempted to join but was denied due to player being deathbanned");

			return;
		}

		long millis = System.currentTimeMillis();
		long lastAttemptedJoinMillis = this.lastAttemptedJoinMap.get(uuid);

		if ((lastAttemptedJoinMillis != this.lastAttemptedJoinMap.getNoEntryValue())
				&& (lastAttemptedJoinMillis - millis < LIFE_USE_DELAY_MILLIS)) {
			this.lastAttemptedJoinMap.remove(uuid);
			user.removeDeathban();
			lives = this.plugin.getDeathbanManager().takeLives(uuid, 1);

			event.setResult(PlayerLoginEvent.Result.ALLOWED);
			return;
		}

		// The user has lives, but just in case they didn't want them to use, tell them
		// to join again in the next 30 seconds.
		String reason = deathban.getReason();
		this.lastAttemptedJoinMap.put(uuid, millis + LIFE_USE_DELAY_MILLIS);

		System.out.println(
				event.getPlayer().getName() + " attempted to join but was denied due to player being deathbanned");

		event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
				"You are currently death-banned" + (reason != null ? " for " + reason : "") + ".\n" + ChatColor.WHITE
						+ formattedRemaining + " remaining.\n" + ChatColor.RED + "You currently have "
						+ (lives <= 0 ? "no" : lives) + " lives.\n\n" + "You may use a life by reconnecting within "
						+ ChatColor.YELLOW + DeathbanListener.LIFE_USE_DELAY_WORDS + ChatColor.RED + ".");
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Deathban deathban = plugin.getDeathbanManager().applyDeathBan(player, event.getDeathMessage());

		if (player.hasPermission(DeathbanListener.DEATH_BAN_BYPASS_PERMISSION) || player.isOp()
				|| ConfigurationService.KIT_MAP)
			return;

		long remaining = deathban.getRemaining();
		if (remaining <= 0L) {
			return;
		}

		if (DeathbanListener.RESPAWN_KICK_DELAY_MILLIS <= 0L
				|| remaining < DeathbanListener.RESPAWN_KICK_DELAY_MILLIS) {
			this.handleKick(player, deathban);
			return;
		}

		DeathbanListener.this.handleKick(player, deathban);

		// Let the player see the death screen for 10 seconds
		this.respawnTickTasks.put(player.getUniqueId(), new BukkitRunnable() {
			@Override
			public void run() {
				DeathbanListener.this.handleKick(player, deathban);
			}
		}.runTaskLater(plugin, DeathbanListener.RESPAWN_KICK_DELAY_TICKS).getTaskId());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerRequestRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		FactionUser user = this.plugin.getUserManager().getUser(player.getUniqueId());
		Deathban deathban = user.getDeathban();
		if (deathban != null && deathban.getRemaining() > 0L) {
			if (player.hasPermission(DeathbanListener.DEATH_BAN_BYPASS_PERMISSION) || ConfigurationService.KIT_MAP) {
				this.cancelRespawnKickTask(player);
				user.removeDeathban();
				player.sendMessage(ChatColor.RED + "You would be death-banned, but you have access to bypass.");
				// new DelayedMessageRunnable(plugin, player, ChatColor.RED + "You would be
				// death-banned for " + deathban.getReason() + ChatColor.RED + ", but you have
				// access to bypass.").runTask(this.plugin);

				return;
			}

			// TODO: FIX setcancelled
			// event.setCancelled(true);
			this.handleKick(player, deathban);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		this.cancelRespawnKickTask(event.getPlayer());
	}

	private void cancelRespawnKickTask(Player player) {
		int taskId = this.respawnTickTasks.remove(player.getUniqueId());
		if (taskId != this.respawnTickTasks.getNoEntryValue()) {
			Bukkit.getScheduler().cancelTask(taskId);
			System.out.println("cancelled");
		}
	}

	private void handleKick(Player player, Deathban deathban) {
		System.out.println("hola com estas");

		if (this.plugin.getEotwHandler().isEndOfTheWorld()) {
			player.kickPlayer(ChatColor.RED
					+ "Deathbanned for the entirety of the map due to EOTW.\nVisit the forums for SOTW info!");
		} else {
			player.kickPlayer(ChatColor.RED + "Deathbanned for "
					+ DurationFormatter.getRemaining(deathban.getRemaining(), true, false) + ": " + ChatColor.YELLOW
					+ deathban.getReason());
		}
	}
}