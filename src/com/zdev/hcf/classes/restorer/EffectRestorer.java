package com.zdev.hcf.classes.restorer;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionEffectExpireEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.zdev.hcf.Base;
import com.zdev.hcf.classes.event.PvpClassUnequipEvent;

public class EffectRestorer implements Listener {

	private Table<UUID, PotionEffectType, PotionEffect> restores;

	public EffectRestorer() {
		this.restores = HashBasedTable.create();
		Bukkit.getPluginManager().registerEvents(this, Base.getPlugin());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPvpClassUnequip(PvpClassUnequipEvent event) {
		this.restores.rowKeySet().remove(event.getPlayer().getUniqueId());
	}

	public void setRestoreEffect(Player player, PotionEffect effect) {
		if (effect == null)
			return;

		boolean shouldCancel = true;
		Collection<PotionEffect> activeList = player.getActivePotionEffects();
		for (PotionEffect active : activeList) {
			if (active.getType().equals(effect.getType())) {
				if (effect.getAmplifier() < active.getAmplifier())
					return;

				if (effect.getAmplifier() == active.getAmplifier() && effect.getDuration() < active.getDuration())
					return;

				this.restores.put(player.getUniqueId(), active.getType(), active);
				shouldCancel = false;
			}
		}
		player.addPotionEffect(effect, true);
		if (shouldCancel && effect.getDuration() > 100 && effect.getDuration() < TimeUnit.MINUTES.toMillis(8)) {
			this.restores.remove(player.getUniqueId(), effect.getType());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPotionEffectExpire(PotionEffectExpireEvent event) {
		LivingEntity livingEntity = event.getEntity();
		if (livingEntity instanceof Player) {
			Player player = (Player) livingEntity;
			PotionEffect previous = this.restores.remove(player.getUniqueId(), event.getEffect().getType());
			if (previous != null) {
				event.setCancelled(true);
				new BukkitRunnable() {

					@Override
					public void run() {
						player.addPotionEffect(previous, true);
					}
				}.runTask(Base.getPlugin());
			}
		}
	}

}
