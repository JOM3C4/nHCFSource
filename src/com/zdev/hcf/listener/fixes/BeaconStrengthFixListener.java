package com.zdev.hcf.listener.fixes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionEffectAddEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.zdev.hcf.Base;

public class BeaconStrengthFixListener implements Listener {

	private static final int VANILLA_BEACON_STRENGTH_LIMIT = 2;

	private final Base plugin;

	public BeaconStrengthFixListener(Base plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPotionEffectAdd(PotionEffectAddEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity instanceof Player && event.getCause() == PotionEffectAddEvent.EffectCause.BEACON) {
			int limit = 1;

			if (limit <= 0) {
				event.setCancelled(true);
				return;
			}

			limit--; // we do this because the numbering for potion effects are weird in bukkit
			// amplifier of 0 is a level 1 potion, amplifier of 1 is a level 2, etc..
			// so let's not confuse the config editor.
			if (limit >= BeaconStrengthFixListener.VANILLA_BEACON_STRENGTH_LIMIT) {
				return;
			}

			PotionEffect effect = event.getEffect();
			if (effect.getAmplifier() > limit && effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
				entity.addPotionEffect(
						new PotionEffect(effect.getType(), effect.getDuration(), limit, effect.isAmbient()));
				event.setCancelled(true);
			}
		}
	}
}
