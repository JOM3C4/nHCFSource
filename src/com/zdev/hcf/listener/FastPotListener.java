package com.zdev.hcf.listener;

import org.bukkit.plugin.java.*;
import org.bukkit.util.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;

public class FastPotListener extends JavaPlugin implements Listener {
	public void onEnable() {
		this.saveDefaultConfig();
		final double speed = 1.0;
		this.getServer().getPluginManager().registerEvents((Listener) new Listener() {
			@EventHandler
			void onProjectileLaunch(final ProjectileLaunchEvent event) {
				if (event.getEntityType() == EntityType.SPLASH_POTION) {
					final Projectile projectile = event.getEntity();
					if (projectile.getShooter() instanceof Player && ((Player) projectile.getShooter()).isSprinting()) {
						final Vector velocity = projectile.getVelocity();
						velocity.setY(velocity.getY() - speed);
						projectile.setVelocity(velocity);
					}
				}
			}

			@EventHandler
			void onPotionSplash(final PotionSplashEvent event) {
				if (event.getEntity().getShooter() instanceof Player) {
					final Player shooter = (Player) event.getEntity().getShooter();
					if (shooter.isSprinting() && event.getIntensity((LivingEntity) shooter) > 0.3) {
						event.setIntensity((LivingEntity) shooter, 0.3);
					}
				}
			}
		}, (Plugin) this);
	}
}
