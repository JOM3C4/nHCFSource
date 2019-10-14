package com.zdev.hcf.listener.fixes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StrengthFixListener implements Listener {

	@EventHandler
	public void onPlayerDamage(final EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			final Player player = (Player) event.getDamager();
			if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
				for (final PotionEffect Effect : player.getActivePotionEffects()) {
					if (Effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
						final double DamagePercentage = (Effect.getAmplifier() + 1) * 1.3 + 1.0;
						int NewDamage;
						if (event.getDamage() / DamagePercentage <= 1.0) {
							NewDamage = (Effect.getAmplifier() + 1) * 3 + 1;
						} else {
							NewDamage = (int) (event.getDamage() / DamagePercentage) + (Effect.getAmplifier() + 1) * 3;
						}
						event.setDamage((double) NewDamage);
						break;
					}
				}
			}
		}
	}
}