package com.zdev.hcf.timer.type;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutSetSlot;
import net.minecraft.server.v1_7_R4.PlayerInventory;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.Iterator;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.event.player.PlayerItemHeldEvent;
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

import com.zdev.hcf.Base;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.sotw.SotwTimer;
import com.zdev.hcf.util.CC;
import com.zdev.hcf.util.Messager;

import org.bukkit.entity.Snowball;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.com.google.common.cache.CacheBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.event.Listener;

public class SnowBall implements Listener {
	private final ConcurrentMap<Object, Object> snowballItemNameFaker;
	public Base plugin;
	public int defaultCooldown;
	private static Map<String, Long> cooldown;

	static {
		SnowBall.cooldown = new HashMap<String, Long>();
	}

	public SnowBall(final Base plugin) {
		this.defaultCooldown = 10;
		this.plugin = plugin;
		this.snowballItemNameFaker = (ConcurrentMap<Object, Object>) CacheBuilder.newBuilder()
				.expireAfterWrite((long) (this.defaultCooldown + 1500 + 5000), TimeUnit.MILLISECONDS).build().asMap();
	}

	public static Map<String, Long> getCooldown() {
		return SnowBall.cooldown;
	}

	@EventHandler
	@Deprecated
	public void onProjectileLaunchs(final ProjectileLaunchEvent event) {
		if (event.isCancelled() || !(event.getEntity().getShooter() instanceof Player)) {
			return;
		}
		final Player shooter = (Player) event.getEntity().getShooter();
		if (event.getEntity() instanceof Snowball) {
			final FileConfiguration file = Base.getPlugin().getConfig();
			if (file.getBoolean("users-can-use-snowball")) {
				SnowBall.cooldown.put(shooter.getName(), System.currentTimeMillis() + 16000L);
				final SotwTimer.SotwRunnable sotw = Base.getPlugin().getSotwTimer().getSotwRunnable();
				final PvPTimerProtection pvpprot = Base.getPlugin().getTimerManager().pvpProtectionTimer;
				if (pvpprot.hasCooldown(shooter)) {
					event.setCancelled(true);
					Messager.player(shooter, "&cYou cannot use the &b&lSnowball &cwith &a&lPvP Timer&c.");
					SnowBall.cooldown.remove(shooter.getName());
					shooter.updateInventory();
				}
				if (sotw != null) {
					event.setCancelled(true);
					Messager.player(shooter, "&cYou cannot use the &b&lSnowball &cif &a&lSOTW Timer &cis enabled!");
					SnowBall.cooldown.remove(shooter.getName());
					shooter.updateInventory();
				}
				if (Base.getPlugin().getFactionManager().getFactionAt(shooter.getLocation()).isSafezone()) {
					event.setCancelled(true);
					Messager.player(shooter, "&cYou cannot use the &b&lSnowball &cin &a&lSpawn&c!");
					SnowBall.cooldown.remove(shooter.getName());
					shooter.updateInventory();
				}
				this.startDisplaying(shooter);
				new BukkitRunnable() {
					public void run() {
						if (SnowBall.isOnCooldown(shooter) && SnowBall.getCooldownDouble(shooter) == 0.1) {
							Messager.player(shooter, "&7Your &B&lSnowball &7cooldown has expired.");
							SnowBall.this.stopDisplaying(shooter);
							this.cancel();
						}
					}
				}.runTaskTimer((Plugin) Base.getPlugin(), 1L, 1L);
			} else {
				if (!shooter.hasPermission("hcf.use.snowball") || file.getBoolean("users-can-use-snowball")) {
					return;
				}
				SnowBall.cooldown.put(shooter.getName(), System.currentTimeMillis() + 16000L);
				final SotwTimer.SotwRunnable sotw = Base.getPlugin().getSotwTimer().getSotwRunnable();
				final PvPTimerProtection pvpprot = Base.getPlugin().getTimerManager().pvpProtectionTimer;
				if (pvpprot.hasCooldown(shooter)) {
					event.setCancelled(true);
					Messager.player(shooter, "&cYou cannot use the &B&lSnowball &cwith &a&lPvP Timer&c.");
					SnowBall.cooldown.remove(shooter.getName());
					shooter.updateInventory();
				}
				if (sotw != null) {
					event.setCancelled(true);
					Messager.player(shooter, "&cYou cannot use the &b&lSnowball &cif &a&lSOTW Timer &cis enabled!");
					SnowBall.cooldown.remove(shooter.getName());
					shooter.updateInventory();
				}
				if (Base.getPlugin().getFactionManager().getFactionAt(shooter.getLocation()).isSafezone()) {
					event.setCancelled(true);
					Messager.player(shooter, "&cYou cannot use the &5&lEgg Teleport &cin &a&lSpawn&c!");
					SnowBall.cooldown.remove(shooter.getName());
					shooter.updateInventory();
				}
				this.startDisplaying(shooter);
				new BukkitRunnable() {
					public void run() {
						if (SnowBall.isOnCooldown(shooter) && SnowBall.getCooldownDouble(shooter) == 0.1) {
							Messager.player(shooter, "&7Your &b&lSnowball &7cooldown has expired.");
							SnowBall.this.stopDisplaying(shooter);
							this.cancel();
						}
					}
				}.runTaskTimer((Plugin) Base.getPlugin(), 1L, 1L);
			}
		}
	}

	public static void refundSnowball(final Player player) {
		final SnowBall ball = new SnowBall(Base.getPlugin());
		ball.stopDisplaying(player);
		getCooldown().remove(player.getName());
		ball.snowballItemNameFaker.remove(player.getName());
		player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.SNOW_BALL, 1) });
		player.updateInventory();
		Messager.player(player, "&cYour Snowball has been refunded!");
	}

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final FileConfiguration file = Base.getPlugin().getConfig();
		if (file.getBoolean("users-can-use-snowball")) {
			if (event.getItem() == null
					|| (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
					|| event.getItem().getType() != Material.SNOW_BALL) {
				return;
			}
			if (SnowBall.cooldown.containsKey(event.getPlayer().getName())
					&& SnowBall.cooldown.get(event.getPlayer().getName()) > System.currentTimeMillis()) {
				final long millisLeft = SnowBall.cooldown.get(event.getPlayer().getName()) - System.currentTimeMillis();
				final double value = millisLeft / 1000.0;
				final double sec = Math.round(10.0 * value) / 10.0;
				event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cYou cannot use &b&lSnowball &cfor another &l" + sec + "s."));
				event.setCancelled(true);
				event.getPlayer().updateInventory();
			}
		} else {
			if (!player.hasPermission("hcf.use.snowball") || file.getBoolean("users-can-use-snowball")) {
				return;
			}
			if (event.getItem() == null
					|| (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
					|| event.getItem().getType() != Material.SNOW_BALL) {
				return;
			}
			if (SnowBall.cooldown.containsKey(event.getPlayer().getName())
					&& SnowBall.cooldown.get(event.getPlayer().getName()) > System.currentTimeMillis()) {
				final long millisLeft = SnowBall.cooldown.get(event.getPlayer().getName()) - System.currentTimeMillis();
				final double value = millisLeft / 1000.0;
				final double sec = Math.round(10.0 * value) / 10.0;
				event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cYou cannot use &b&lSnowball &cfor another &l" + sec + "s."));
				event.setCancelled(true);
				event.getPlayer().updateInventory();
			}
		}
	}

	@EventHandler
	public void setEffects(final EntityDamageByEntityEvent event) {
		final Entity damager = event.getDamager();
		final Entity damaged = event.getEntity();
		if (damager instanceof Snowball && damaged instanceof Player) {
			final Player player = (Player) damaged;
			final Snowball snowball = (Snowball) damager;
			final ProjectileSource source = (ProjectileSource) snowball.getShooter();
			final Player shooter = (Player) source;
			final FileConfiguration file = Base.getPlugin().getConfig();
			if (file.getBoolean("users-can-use-snowball")) {
				final PlayerFaction damagedFaction = Base.getPlugin().getFactionManager()
						.getPlayerFaction(player.getUniqueId());
				final PlayerFaction damagerFaction = Base.getPlugin().getFactionManager()
						.getPlayerFaction(shooter.getUniqueId());
				final PvPTimerProtection pvpprot = Base.getPlugin().getTimerManager().pvpProtectionTimer;
				if (Base.getPlugin().getFactionManager().getFactionAt(player.getLocation()).isSafezone()
						|| Base.getPlugin().getFactionManager().getFactionAt(shooter.getLocation()).isSafezone()) {
					refundSnowball(shooter);
				} else if (damagerFaction != null && damagerFaction.equals(damagedFaction)) {
					refundSnowball(shooter);
				} else if (damagerFaction != null && damagerFaction != damagedFaction) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0));
					this.sendDamageBySnowballMessage(shooter, player);
				} else if (damagerFaction == null) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0));
					this.sendDamageBySnowballMessage(shooter, player);
				} else if (damagerFaction != null
						&& damagerFaction.getAlliedFactions().contains(damagedFaction.getUniqueID())) {
					refundSnowball(shooter);
				} else if (pvpprot.hasCooldown(player)) {
					refundSnowball(shooter);
					Messager.player(shooter, "&cThis player contains &a&lPvP Timer&c.");
				} else {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0));
					this.sendDamageBySnowballMessage(shooter, player);
				}
			} else {
				if (!shooter.hasPermission("hcf.use.snowball") || file.getBoolean("users-can-use-snowball")) {
					return;
				}
				final PlayerFaction damagedFaction = Base.getPlugin().getFactionManager()
						.getPlayerFaction(player.getUniqueId());
				final PlayerFaction damagerFaction = Base.getPlugin().getFactionManager()
						.getPlayerFaction(shooter.getUniqueId());
				final PvPTimerProtection pvpprot = Base.getPlugin().getTimerManager().pvpProtectionTimer;
				if (Base.getPlugin().getFactionManager().getFactionAt(player.getLocation()).isSafezone()
						|| Base.getPlugin().getFactionManager().getFactionAt(shooter.getLocation()).isSafezone()) {
					refundSnowball(shooter);
				} else if (damagerFaction != null && damagerFaction.equals(damagedFaction)) {
					refundSnowball(shooter);
				} else if (damagerFaction != null && damagerFaction != damagedFaction) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0));
					this.sendDamageBySnowballMessage(shooter, player);
				} else if (damagerFaction == null) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0));
					this.sendDamageBySnowballMessage(shooter, player);
				} else if (damagerFaction != null
						&& damagerFaction.getAlliedFactions().contains(damagedFaction.getUniqueID())) {
					refundSnowball(shooter);
				} else if (pvpprot.hasCooldown(player)) {
					refundSnowball(shooter);
					Messager.player(shooter, "&cThis player contains &a&lPvP Timer&c.");
				} else {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 0));
					this.sendDamageBySnowballMessage(shooter, player);
				}
			}
		}
	}

	public void sendDamageBySnowballMessage(final Player shooter, final Player player) {
		if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
			shooter.sendMessage(CC.translate("&cYou have damaged &f???&c with a &b&lSnowball&c."));
		} else {
			shooter.sendMessage(CC.translate("&cYou have damaged &f" + player.getName() + "&c with a &b&lSnowball&c."));
		}
		if (shooter.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou has been damaged by &f???&c with a &b&lSnowball&c."));
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou has been damaged by &f" + shooter.getName() + "&c with a &b&lSnowball&c."));
		}
	}

	public static boolean isOnCooldown(final Player player) {
		return SnowBall.cooldown.containsKey(player.getName())
				&& SnowBall.cooldown.get(player.getName()) > System.currentTimeMillis();
	}

	public static String getCooldown(final Player player) {
		final long millisLeft = SnowBall.cooldown.get(player.getName()) - System.currentTimeMillis();
		final double value = millisLeft / 1000.0;
		final double sec = Math.round(10.0 * value) / 10.0;
		return CC.translate("&c" + sec + "s");
	}

	public static double getCooldownDouble(final Player player) {
		final long millisLeft = SnowBall.cooldown.get(player.getName()) - System.currentTimeMillis();
		final double value = millisLeft / 1000.0;
		final double sec = Math.round(10.0 * value) / 10.0;
		return sec;
	}

	public static double getCooldownInt(final Player player) {
		final long millisLeft = SnowBall.cooldown.get(player.getName()) - System.currentTimeMillis();
		final int value = (int) (millisLeft / 1000.0);
		final int sec = (int) (Math.round(10.0 * value) / 10.0);
		return sec;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerItemHeld(final PlayerItemHeldEvent event) {
		final Player player = event.getPlayer();
		final Object pearlNameFaker = this.snowballItemNameFaker.get(player.getUniqueId());
		if (pearlNameFaker != null) {
			final int previousSlot = event.getPreviousSlot();
			final ItemStack item = player.getInventory().getItem(previousSlot);
			if (item == null) {
				return;
			}
			((SnowballNameFaker) pearlNameFaker).setFakeItem(CraftItemStack.asNMSCopy(item), previousSlot);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onInventoryDrag(final InventoryDragEvent event) {
		final HumanEntity humanEntity = event.getWhoClicked();
		if (humanEntity instanceof Player) {
			final Player player = (Player) humanEntity;
			final Object pearlNameFaker = this.snowballItemNameFaker.get(player.getUniqueId());
			if (pearlNameFaker == null) {
				return;
			}
			for (final Map.Entry<Integer, ItemStack> entry : event.getNewItems().entrySet()) {
				if (entry.getKey() == player.getInventory().getHeldItemSlot()) {
					((SnowballNameFaker) pearlNameFaker).setFakeItem(CraftItemStack.asNMSCopy(player.getItemInHand()),
							player.getInventory().getHeldItemSlot());
					break;
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onInventoryClick(final InventoryClickEvent event) {
		final HumanEntity humanEntity = event.getWhoClicked();
		if (humanEntity instanceof Player) {
			final Player player = (Player) humanEntity;
			final Object ballNameFaker = this.snowballItemNameFaker.get(player.getUniqueId());
			if (ballNameFaker == null) {
				return;
			}
			final int heldSlot = player.getInventory().getHeldItemSlot();
			if (event.getSlot() == heldSlot) {
				((SnowballNameFaker) ballNameFaker).setFakeItem(CraftItemStack.asNMSCopy(player.getItemInHand()),
						heldSlot);
			} else if (event.getHotbarButton() == heldSlot) {
				((SnowballNameFaker) ballNameFaker).setFakeItem(CraftItemStack.asNMSCopy(event.getCurrentItem()),
						event.getSlot());
				new BukkitRunnable() {
					public void run() {
						player.updateInventory();
					}
				}.runTask((Plugin) this.plugin);
			}
		}
	}

	public void startDisplaying(final Player player) {
		if (isOnCooldown(player)) {
			final SnowballNameFaker pearlNameFaker = new SnowballNameFaker(player);
			if (this.snowballItemNameFaker.putIfAbsent(player.getUniqueId(), pearlNameFaker) == null) {
				final long ticks = (((CraftPlayer) player).getHandle().playerConnection.networkManager
						.getVersion() >= 47) ? 20L : 2L;
				pearlNameFaker.runTaskTimerAsynchronously((Plugin) this.plugin, ticks, ticks);
			}
		}
	}

	public void stopDisplaying(final Player player) {
		final Object pearlNameFaker = this.snowballItemNameFaker.remove(player.getUniqueId());
		if (pearlNameFaker != null) {
			((BukkitRunnable) pearlNameFaker).cancel();
		}
	}

	public static class SnowballNameFaker extends BukkitRunnable {
		private final Player player;

		public SnowballNameFaker(final Player player) {
			this.player = player;
		}

		public void run() {
			final ItemStack stack = this.player.getItemInHand();
			if (stack != null && stack.getType() == Material.SNOW_BALL) {
				net.minecraft.server.v1_7_R4.ItemStack item = CraftItemStack.asNMSCopy(stack);
				if (SnowBall.isOnCooldown(this.player)) {
					item = item.cloneItemStack();
					item.c(CC.translate("&bSnowball Cooldown&7: &c" + SnowBall.getCooldown(this.player)));
					this.setFakeItem(item, this.player.getInventory().getHeldItemSlot());
				} else {
					this.cancel();
				}
			}
		}

		public synchronized void cancel() throws IllegalStateException {
			super.cancel();
			this.setFakeItem(CraftItemStack.asNMSCopy(this.player.getItemInHand()),
					this.player.getInventory().getHeldItemSlot());
		}

		public void setFakeItem(final net.minecraft.server.v1_7_R4.ItemStack nms, int index) {
			final EntityPlayer entityPlayer = ((CraftPlayer) this.player).getHandle();
			if (index < PlayerInventory.getHotbarSize()) {
				index += 36;
			} else if (index > 35) {
				index = 8 - (index - 36);
			}
			entityPlayer.playerConnection
					.sendPacket((Packet) new PacketPlayOutSetSlot(entityPlayer.activeContainer.windowId, index, nms));
		}
	}
}
