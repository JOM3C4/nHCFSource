package com.zdev.hcf.timer.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.EquipmentSetEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.zdev.hcf.Base;
import com.zdev.hcf.classes.PvpClass;
import com.zdev.hcf.timer.PlayerTimer;
import com.zdev.hcf.timer.TimerCooldown;
import com.zdev.hcf.util.Config;

import net.minecraft.util.com.google.common.base.Preconditions;

/**
 * Timer that handles {@link PvpClass} warmups.
 */
public class PvpClassWarmupTimer extends PlayerTimer implements Listener {

	protected final Map<UUID, PvpClass> classWarmups = new HashMap<>();

	private final Base plugin;

	public PvpClassWarmupTimer(Base plugin) {
		super("Warmup", TimeUnit.SECONDS.toMillis(10L), false);
		this.plugin = plugin;

		// Re-equip the applicable class for every player during reloads.
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					attemptEquip(player);
				}
			}
		}.runTaskLater(plugin, 10L);
	}

	@Override
	public void onDisable(Config config) {
		super.onDisable(config);
		this.classWarmups.clear();
	}

	@Override
	public String getScoreboardPrefix() {
		return ChatColor.AQUA.toString() + ChatColor.BOLD;
	}

	@Override
	public TimerCooldown clearCooldown(UUID playerUUID) {
		TimerCooldown runnable = super.clearCooldown(playerUUID);
		if (runnable != null) {
			this.classWarmups.remove(playerUUID);
			return runnable;
		}

		return null;
	}

	@Override
	public void onExpire(UUID userUUID) {
		Player player = Bukkit.getPlayer(userUUID);
		if (player == null)
			return;

		PvpClass pvpClass = this.classWarmups.remove(userUUID);
		Preconditions.checkNotNull(pvpClass, "Attempted to equip a class for %s, but nothing was added",
				player.getName());
		this.plugin.getPvpClassManager().setEquippedClass(player, pvpClass);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerKick(PlayerQuitEvent event) {
		this.plugin.getPvpClassManager().setEquippedClass(event.getPlayer(), null);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		this.attemptEquip(event.getPlayer());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onEquipmentSet(EquipmentSetEvent event) {
		HumanEntity humanEntity = event.getHumanEntity();
		if (humanEntity instanceof Player) {
			this.attemptEquip((Player) humanEntity);
		}
	}

	private void attemptEquip(Player player) {
		PvpClass current = plugin.getPvpClassManager().getEquippedClass(player);
		if (current != null) {
			if (current.isApplicableFor(player)) {
				return;
			}

			this.plugin.getPvpClassManager().setEquippedClass(player, null);
		} else if ((current = classWarmups.get(player.getUniqueId())) != null) {
			if (current.isApplicableFor(player)) {
				return;
			}

			this.clearCooldown(player.getUniqueId());
		}

		Collection<PvpClass> pvpClasses = plugin.getPvpClassManager().getPvpClasses();
		for (PvpClass pvpClass : pvpClasses) {
			if (pvpClass.isApplicableFor(player)) {
				this.classWarmups.put(player.getUniqueId(), pvpClass);
				this.setCooldown(player, player.getUniqueId(), pvpClass.getWarmupDelay(), false);
				break;
			}
		}
	}
}
