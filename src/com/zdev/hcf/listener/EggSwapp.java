package com.zdev.hcf.listener;

import org.bukkit.util.Vector;

import com.zdev.hcf.Base;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.sotw.SotwTimer;
import com.zdev.hcf.timer.type.PvPTimerProtection;
import com.zdev.hcf.util.CC;
import com.zdev.hcf.util.Messager;
import com.zdev.hcf.util.Swapp;

import org.bukkit.Location;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Egg;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.event.Listener;

public class EggSwapp implements Listener {
	public static Map<String, Long> cooldown;

	static {
		EggSwapp.cooldown = new HashMap<String, Long>();
	}

	public static Map<String, Long> getCooldown() {
		return EggSwapp.cooldown;
	}

	public static boolean isOnCooldown(final Player player) {
		return EggSwapp.cooldown.containsKey(player.getName())
				&& EggSwapp.cooldown.get(player.getName()) > System.currentTimeMillis();
	}

	public static String getCooldown(final Player player) {
		final long millisLeft = EggSwapp.cooldown.get(player.getName()) - System.currentTimeMillis();
		final double value = millisLeft / 1000.0;
		final double sec = Math.round(5.0 * value) / 5.0;
		return CC.translate("&c" + sec + "s");
	}

	public static double getCooldownDouble(final Player player) {
		final long millisLeft = EggSwapp.cooldown.get(player.getName()) - System.currentTimeMillis();
		final double value = millisLeft / 1000.0;
		final double sec = Math.round(5.0 * value) / 5.0;
		return sec;
	}

	public static double getCooldownInt(final Player player) {
		final long millisLeft = EggSwapp.cooldown.get(player.getName()) - System.currentTimeMillis();
		final int value = (int) (millisLeft / 1000.0);
		final int sec = (int) (Math.round(5.0 * value) / 5.0);
		return sec;
	}

	@EventHandler
	@Deprecated
	public void onProjectileLaunchs(final ProjectileLaunchEvent event) {
		if (event.isCancelled() || !(event.getEntity().getShooter() instanceof Player)) {
			return;
		}
		final Player shooter = (Player) event.getEntity().getShooter();
		if (event.getEntity() instanceof Egg) {
			final FileConfiguration file = Base.getPlugin().getConfig();
			if (file.getBoolean("users-can-use-eggteleport")) {
				EggSwapp.cooldown.put(shooter.getName(), System.currentTimeMillis() + 20000L);
				final SotwTimer.SotwRunnable sotw = Base.getPlugin().getSotwTimer().getSotwRunnable();
				final PvPTimerProtection pvpprot = Base.getPlugin().getTimerManager().pvpProtectionTimer;
				if (pvpprot.hasCooldown(shooter)) {
					event.setCancelled(true);
					Messager.player(shooter, "&cYou cannot use the &B&lEgg Teleport &cwith &a&lPvP Timer&c.");
					EggSwapp.cooldown.remove(shooter.getName());
					shooter.updateInventory();
				}
				if (sotw != null) {
					event.setCancelled(true);
					Messager.player(shooter, "&cYou cannot use the &B&lEgg Teleport &cif &a&lSOTW Timer &cis enabled!");
					EggSwapp.cooldown.remove(shooter.getName());
					shooter.updateInventory();
				}
				if (Base.getPlugin().getFactionManager().getFactionAt(shooter.getLocation()).isSafezone()) {
					event.setCancelled(true);
					Messager.player(shooter, "&cYou cannot use the &B&lEgg Teleport &cin &a&lSpawn&c!");
					EggSwapp.cooldown.remove(shooter.getName());
					shooter.updateInventory();
				}
				new BukkitRunnable() {
					public void run() {
						if (EggSwapp.isOnCooldown(shooter) && EggSwapp.getCooldownDouble(shooter) == 0.1) {
							Messager.player(shooter, "&cYour &B&lEgg Teleport &7cooldown has expired.");
							this.cancel();
						}
					}
				}.runTaskTimer((Plugin) Base.getPlugin(), 1L, 1L);
			} else {
				if (!shooter.hasPermission("hcf.use.eggteleport") || file.getBoolean("users-can-use-eggteleport")) {
					return;
				}
				EggSwapp.cooldown.put(shooter.getName(), System.currentTimeMillis() + 20000L);
				final SotwTimer.SotwRunnable sotw = Base.getPlugin().getSotwTimer().getSotwRunnable();
				final PvPTimerProtection pvpprot = Base.getPlugin().getTimerManager().pvpProtectionTimer;
				if (pvpprot.hasCooldown(shooter)) {
					event.setCancelled(true);
					Messager.player(shooter, "&cYou cannot use the &5&lEgg Teleport &cwith &a&lPvP Timer&c.");
					EggSwapp.cooldown.remove(shooter.getName());
					shooter.updateInventory();
				}
				if (sotw != null) {
					event.setCancelled(true);
					Messager.player(shooter, "&cYou cannot use the &5&lEgg Teleport &cif &a&lSOTW Timer &cis enabled!");
					EggSwapp.cooldown.remove(shooter.getName());
					shooter.updateInventory();
				}
				if (Base.getPlugin().getFactionManager().getFactionAt(shooter.getLocation()).isSafezone()) {
					event.setCancelled(true);
					Messager.player(shooter, "&cYou cannot use the &B&lEgg Teleport &cin &a&lSpawn&c!");
					EggSwapp.cooldown.remove(shooter.getName());
					shooter.updateInventory();
				}
				new BukkitRunnable() {
					public void run() {
						if (EggSwapp.isOnCooldown(shooter) && EggSwapp.getCooldownDouble(shooter) == 0.1) {
							Messager.player(shooter, "&cYour &b&lEgg Teleport &7cooldown has expired.");
							this.cancel();
						}
					}
				}.runTaskTimer((Plugin) Base.getPlugin(), 1L, 1L);
			}
		}
	}

	public static void refund(final Player player) {
		player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.EGG, 1) });
		player.updateInventory();
		Messager.player(player, "&cYour &5&lEgg Teleport &chas been refunded!");
		EggSwapp.cooldown.remove(player.getName());
	}

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		final Player shooter = event.getPlayer();
		final FileConfiguration file = Base.getPlugin().getConfig();
		if (file.getBoolean("users-can-use-eggteleport")) {
			if (event.getItem() == null
					|| (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
					|| event.getItem().getType() != Material.EGG) {
				return;
			}
			if (EggSwapp.cooldown.containsKey(event.getPlayer().getName())
					&& EggSwapp.cooldown.get(event.getPlayer().getName()) > System.currentTimeMillis()) {
				final long millisLeft = EggSwapp.cooldown.get(event.getPlayer().getName()) - System.currentTimeMillis();
				final double value = millisLeft / 1000.0;
				final double sec = Math.round(20.0 * value) / 10.0;
				event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cYou cannot use &5&lEgg Teleport &cfor another &l" + sec + "s."));
				event.setCancelled(true);
				event.getPlayer().updateInventory();
			}
		} else {
			if (!shooter.hasPermission("hcf.use.eggteleport") || file.getBoolean("users-can-use-eggteleport")) {
				return;
			}
			if (event.getItem() == null
					|| (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
					|| event.getItem().getType() != Material.EGG) {
				return;
			}
			if (EggSwapp.cooldown.containsKey(event.getPlayer().getName())
					&& EggSwapp.cooldown.get(event.getPlayer().getName()) > System.currentTimeMillis()) {
				final long millisLeft = EggSwapp.cooldown.get(event.getPlayer().getName()) - System.currentTimeMillis();
				final double value = millisLeft / 1000.0;
				final double sec = Math.round(10.0 * value) / 10.0;
				event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cYou cannot use &b&lEgg Teleport &cfor another &l" + sec + "s."));
				event.setCancelled(true);
				event.getPlayer().updateInventory();
			}
		}
	}

	@EventHandler
	public void onDamge(final EntityDamageByEntityEvent event) {
		final Entity en = event.getEntity();
		final Entity dm = event.getDamager();
		if (en instanceof Player && dm instanceof Egg) {
			final Player player = (Player) en;
			final Egg egg = (Egg) dm;
			final ProjectileSource source = (ProjectileSource) egg.getShooter();
			final Player shooter = (Player) source;
			final FileConfiguration file = Base.getPlugin().getConfig();
			if (file.getBoolean("users-can-use-eggteleport")) {
				final Player e = Swapp.getPlayerInSight(shooter, 8);
				final PlayerFaction damagedFaction = Base.getPlugin().getFactionManager()
						.getPlayerFaction(player.getUniqueId());
				final PlayerFaction damagerFaction = Base.getPlugin().getFactionManager()
						.getPlayerFaction(shooter.getUniqueId());
				final PvPTimerProtection pvpprot = Base.getPlugin().getTimerManager().pvpProtectionTimer;
				if (Base.getPlugin().getFactionManager().getFactionAt(player.getLocation()).isSafezone()
						|| Base.getPlugin().getFactionManager().getFactionAt(shooter.getLocation()).isSafezone()) {
					refund(shooter);
				} else if (damagerFaction != null && damagerFaction.equals(damagedFaction)) {
					refund(shooter);
				} else if (damagerFaction != null && damagerFaction != damagedFaction) {
					if (e != null) {
						if (!shooter.isOnGround()) {
							refund(shooter);
							Messager.player(shooter, "&cYou cannot use &B&lEgg Teleport &cin ground!");
						} else {
							final Location playerLoc = shooter.getLocation().clone();
							final Location entityLoc = e.getLocation().clone();
							final Vector playerLook = playerLoc.getDirection();
							final Vector playerVec = playerLoc.toVector();
							final Vector entityVec = entityLoc.toVector();
							final Vector toVec = playerVec.subtract(entityVec).normalize();
							e.teleport(playerLoc.setDirection(playerLook.normalize()));
							shooter.teleport(entityLoc.setDirection(toVec));
							e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
							shooter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
							Messager.player(player, "&6&lYou have switched position with &f" + shooter.getName());
							Messager.player(shooter, "&6&lYou have switched position with &f" + e.getName());
						}
					}
				} else if (damagerFaction != null
						&& damagerFaction.getAlliedFactions().contains(damagedFaction.getUniqueID())) {
					refund(shooter);
				} else if (damagerFaction == null) {
					if (e != null) {
						if (!shooter.isOnGround()) {
							Messager.player(shooter, "&cYou cannot use &B&lEgg Teleport &cin ground!");
						} else {
							final Location playerLoc = shooter.getLocation().clone();
							final Location entityLoc = e.getLocation().clone();
							final Vector playerLook = playerLoc.getDirection();
							final Vector playerVec = playerLoc.toVector();
							final Vector entityVec = entityLoc.toVector();
							final Vector toVec = playerVec.subtract(entityVec).normalize();
							e.teleport(playerLoc.setDirection(playerLook.normalize()));
							shooter.teleport(entityLoc.setDirection(toVec));
							e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
							shooter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
							Messager.player(player, "&6&lYou have switched position with &f" + shooter.getName());
							Messager.player(shooter, "&6&lYou have switched position with &f" + e.getName());
						}
					}
				} else if (pvpprot.hasCooldown(player)) {
					refund(shooter);
					Messager.player(shooter, "&cThis player contains &a&lPvP Timer&c.");
				} else if (e != null) {
					final Location playerLoc = shooter.getLocation().clone();
					final Location entityLoc = e.getLocation().clone();
					final Vector playerLook = playerLoc.getDirection();
					final Vector playerVec = playerLoc.toVector();
					final Vector entityVec = entityLoc.toVector();
					final Vector toVec = playerVec.subtract(entityVec).normalize();
					e.teleport(playerLoc.setDirection(playerLook.normalize()));
					shooter.teleport(entityLoc.setDirection(toVec));
					e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
					shooter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
					Messager.player(player, "&6&lYou have switched position with &f" + shooter.getName());
					Messager.player(shooter, "&6&lYou have switched position with &f" + e.getName());
				}
			} else {
				if (!shooter.hasPermission("hcf.use.eggteleport") || file.getBoolean("users-can-use-eggteleport")) {
					return;
				}
				final Player e = Swapp.getPlayerInSight(shooter, 8);
				final PlayerFaction damagedFaction = Base.getPlugin().getFactionManager()
						.getPlayerFaction(player.getUniqueId());
				final PlayerFaction damagerFaction = Base.getPlugin().getFactionManager()
						.getPlayerFaction(shooter.getUniqueId());
				final PvPTimerProtection pvpprot = Base.getPlugin().getTimerManager().pvpProtectionTimer;
				if (Base.getPlugin().getFactionManager().getFactionAt(player.getLocation()).isSafezone()
						|| Base.getPlugin().getFactionManager().getFactionAt(shooter.getLocation()).isSafezone()) {
					refund(shooter);
				} else if (damagerFaction != null && damagerFaction.equals(damagedFaction)) {
					refund(shooter);
				} else if (damagerFaction != null && damagerFaction != damagedFaction) {
					if (e != null) {
						if (!shooter.isOnGround()) {
							refund(shooter);
							Messager.player(shooter, "&cYou cannot use &B&lEgg Teleport &cin ground!");
						} else {
							final Location playerLoc = shooter.getLocation().clone();
							final Location entityLoc = e.getLocation().clone();
							final Vector playerLook = playerLoc.getDirection();
							final Vector playerVec = playerLoc.toVector();
							final Vector entityVec = entityLoc.toVector();
							final Vector toVec = playerVec.subtract(entityVec).normalize();
							e.teleport(playerLoc.setDirection(playerLook.normalize()));
							shooter.teleport(entityLoc.setDirection(toVec));
							e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
							shooter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
							Messager.player(player, "&6&lYou have switched position with &f" + shooter.getName());
							Messager.player(shooter, "&6&lYou have switched position with &f" + e.getName());
						}
					}
				} else if (damagerFaction != null
						&& damagerFaction.getAlliedFactions().contains(damagedFaction.getUniqueID())) {
					refund(shooter);
				} else if (damagerFaction == null) {
					if (e != null) {
						if (!shooter.isOnGround()) {
							Messager.player(shooter, "&cYou cannot use &b&lEgg Teleport &cin ground!");
						} else {
							final Location playerLoc = shooter.getLocation().clone();
							final Location entityLoc = e.getLocation().clone();
							final Vector playerLook = playerLoc.getDirection();
							final Vector playerVec = playerLoc.toVector();
							final Vector entityVec = entityLoc.toVector();
							final Vector toVec = playerVec.subtract(entityVec).normalize();
							e.teleport(playerLoc.setDirection(playerLook.normalize()));
							shooter.teleport(entityLoc.setDirection(toVec));
							e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
							shooter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
							Messager.player(player, "&6&lYou have switched position with &f" + shooter.getName());
							Messager.player(shooter, "&6&lYou have switched position with &f" + e.getName());
						}
					}
				} else if (pvpprot.hasCooldown(player)) {
					refund(shooter);
					Messager.player(shooter, "&cThis player contains &a&lPvP Timer&c.");
				} else if (e != null) {
					if (!shooter.isOnGround()) {
						Messager.player(shooter, "&cYou cannot use &b&lEgg Teleport &cin ground!");
					} else {
						final Location playerLoc = shooter.getLocation().clone();
						final Location entityLoc = e.getLocation().clone();
						final Vector playerLook = playerLoc.getDirection();
						final Vector playerVec = playerLoc.toVector();
						final Vector entityVec = entityLoc.toVector();
						final Vector toVec = playerVec.subtract(entityVec).normalize();
						e.teleport(playerLoc.setDirection(playerLook.normalize()));
						shooter.teleport(entityLoc.setDirection(toVec));
						e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
						shooter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
						Messager.player(player, "&6&lYou have switched position with &f" + shooter.getName());
						Messager.player(shooter, "&6&lYou have switched position with &f" + e.getName());
					}
				}
			}
		}
	}
}
