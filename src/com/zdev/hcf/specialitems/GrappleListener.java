package com.zdev.hcf.specialitems;

import org.apache.commons.lang3.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import com.zdev.hcf.util.DateUtil;

import org.bukkit.Sound;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;
import org.bukkit.ChatColor;
import java.util.Map;
import org.bukkit.event.Listener;

public class GrappleListener implements Listener {
	public static String grappleName;
	public static String grappleLore;
	public static Map<String, Long> grapple;

	static {
		GrappleListener.grappleName = ChatColor.translateAlternateColorCodes('&', "Grappling Hook");
		GrappleListener.grappleLore = ChatColor.translateAlternateColorCodes('&', "This is the original grapple.");
	}

	public GrappleListener() {
		GrappleListener.grapple = new HashMap<String, Long>();
	}

	public boolean isGrappleHook(final Player player) {
		return player.getItemInHand().getItemMeta().getDisplayName()
				.equalsIgnoreCase(ChatColor.GOLD + GrappleListener.grappleName)
				&& player.getItemInHand().getItemMeta().getLore()
						.contains(ChatColor.GRAY + GrappleListener.grappleLore);
	}

	public static Boolean hasCooldown(final Player player) {
		if (GrappleListener.grapple.containsKey(player.getName())
				&& GrappleListener.grapple.get(player.getName()) > System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	public static double getSeconds(final Player player) {
		if (GrappleListener.grapple.containsKey(player.getName())
				&& GrappleListener.grapple.get(player.getName()) > System.currentTimeMillis()) {
			final long diff = GrappleListener.grapple.get(player.getName()) - System.currentTimeMillis();
			final double value = diff / 1000.0;
			final double sec = Math.round(10.0 * value) / 10.0;
			if (diff >= 0L) {
				return sec;
			}
		}
		return -1.0;
	}

	@EventHandler
	public void onInteract(final PlayerInteractEvent event) {
		if (event.getItem() == null
				|| (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
				|| event.getItem().getType() != Material.FISHING_ROD) {
			return;
		}
		if (GrappleListener.grapple.containsKey(event.getPlayer().getName())
				&& GrappleListener.grapple.get(event.getPlayer().getName()) > System.currentTimeMillis()) {
			final long millisLeft = GrappleListener.grapple.get(event.getPlayer().getName())
					- System.currentTimeMillis();
			final double value = millisLeft / 1000.0;
			final double sec = Math.round(10.0 * value) / 10.0;
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou still have &6&l"
					+ GrappleListener.grappleName + " &ccooldown for another &l" + sec + "&r&cs."));
			event.getPlayer().updateInventory();
		}
	}

	@EventHandler
	public void grapple(final PlayerFishEvent event) {
		try {
			final Player p = event.getPlayer();
			final Location h = event.getHook().getLocation();
			final Location h2 = new Location(h.getWorld(), h.getX(), h.getY() + 1.0, h.getZ());
			h.getBlock().getLocation().setY(h.getBlock().getLocation().getY() - 1.0);
			if (p != null && this.isGrappleHook(p) && (event.getState() == PlayerFishEvent.State.FAILED_ATTEMPT
					|| event.getState() == PlayerFishEvent.State.IN_GROUND)) {
				final Location pl = p.getLocation();
				final int x1 = pl.getBlockX();
				final int y1 = pl.getBlockZ();
				final Location loc = event.getHook().getLocation();
				final int x2 = loc.getBlockX();
				final int y2 = loc.getBlockZ();
				loc.setY(loc.getY() - 1.0);
				if ((x1 != x2 && y1 != y2 && loc.getBlock().getType() != Material.AIR
						&& loc.getBlock().getType() != Material.STATIONARY_WATER)
						|| h2.getBlock().getType() != Material.AIR
						|| event.getState() == PlayerFishEvent.State.IN_GROUND) {
					final double kyori = loc.distance(pl);
					final double y3 = loc.getY();
					loc.setY(y3 + 1.0);
					final Vector vec = pl.toVector();
					final Vector vec2 = loc.toVector();
					p.setVelocity(vec2.subtract(vec).normalize().multiply(kyori / 4.5));
					p.playSound(p.getLocation(), Sound.ZOMBIE_INFECT, 10.0f, 2.0f);
					GrappleListener.grapple.put(p.getName(), System.currentTimeMillis() + DateUtil.parseTime("3s"));
				} else {
					p.sendMessage(ChatColor.RED + "Unable to grapple");
					if (hasCooldown(p)) {
						GrappleListener.grapple.remove(p.getName());
					}
				}
			}
		} catch (NullPointerException ex) {
		}
	}

	@EventHandler
	public void onDamage(final EntityDamageEvent event) {
		try {
			if (event.getEntity() instanceof Player) {
				final Player p = (Player) event.getEntity();
				if (p.getItemInHand().getType() != Material.FISHING_ROD || !this.isGrappleHook(p)
						|| event.getCause() != EntityDamageEvent.DamageCause.FALL) {
					return;
				}
				event.setDamage(event.getDamage() / 3.5);
			}
		} catch (NullPointerException ex) {
		}
	}

	@EventHandler
	public void onHold(final PlayerItemHeldEvent event) {
		final Player player = event.getPlayer();
		if (player.getItemInHand().getType() == Material.FISHING_ROD) {
			Player[] onlinePlayers;
			for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i) {
				final Player all = onlinePlayers[i];
				all.playSound(all.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.0f);
			}
		}
	}

	public void pullPlayerSlightly(final Player p, final Location loc) {
		if (loc.getY() > p.getLocation().getY()) {
			p.setVelocity(new Vector(0.0, 0.25, 0.0));
			return;
		}
		final Location playerLoc = p.getLocation();
		final Vector vector = loc.toVector().subtract(playerLoc.toVector());
		p.setVelocity(vector);
	}

	public void pullEntityToLocation(final Player e, final Location loc) {
		final Location entityLoc = e.getLocation();
		entityLoc.setY(entityLoc.getY() + 0.1);
		e.teleport(entityLoc);
		final double g = -0.08;
		final double t = loc.distance(entityLoc);
		final double v_x = (1.0 + 0.1 * t) * (loc.getX() - entityLoc.getX()) / t;
		final double v_y = (1.0 + 0.03 * t) * (loc.getY() - entityLoc.getY()) / t - -0.04 * t;
		final double v_z = (1.0 + 0.1 * t) * (loc.getZ() - entityLoc.getZ()) / t;
		final Vector v = e.getVelocity();
		v.setX(v_x);
		v.setY(v_y);
		v.setZ(v_z);
		e.setVelocity(v);
	}
}
