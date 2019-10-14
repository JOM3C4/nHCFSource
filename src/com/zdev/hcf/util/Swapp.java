package com.zdev.hcf.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.zdev.hcf.util.chat.ChatUtil;

public class Swapp {

	private static boolean hasIntersection(Vector3D start, Vector3D end, Vector3D min, Vector3D max) {
		final double epsilon = 0.0001f;

		Vector3D d = end.subtract(start).multiply(0.5);
		Vector3D e = max.subtract(min).multiply(0.5);
		Vector3D c = start.add(d).subtract(min.add(max).multiply(0.5));
		Vector3D ad = d.abs();

		if (Math.abs(c.getX()) > e.getX() + ad.getX()) {
			return false;
		}

		if (Math.abs(c.getY()) > e.getY() + ad.getY()) {
			return false;
		}

		if (Math.abs(c.getZ()) > e.getX() + ad.getZ()) {
			return false;
		}

		if (Math.abs(d.getY() * c.getZ() - d.getZ() * c.getY()) > e.getY() * ad.getZ() + e.getZ() * ad.getY()
				+ epsilon) {
			return false;
		}

		if (Math.abs(d.getZ() * c.getX() - d.getX() * c.getZ()) > e.getZ() * ad.getX() + e.getX() * ad.getZ()
				+ epsilon) {
			return false;
		}

		if (Math.abs(d.getX() * c.getY() - d.getY() * c.getX()) > e.getX() * ad.getY() + e.getY() * ad.getX()
				+ epsilon) {
			return false;
		}

		return true;
	}

	public static Player getPlayerInSight(Player player, int distance) {
		Location playerLoc = player.getLocation();
		Vector3D playerDirection = new Vector3D(playerLoc.getDirection());
		Vector3D start = new Vector3D(playerLoc);
		Vector3D end = start.add(playerDirection.multiply(distance));
		Player inSight = null;
		for (Entity nearbyEntity : player.getNearbyEntities(distance, distance, distance)) {
			if (nearbyEntity.getType() == EntityType.PLAYER) {
				Vector3D nearbyLoc = new Vector3D(nearbyEntity.getLocation());

				// Bounding box
				Vector3D min = nearbyLoc.subtract(0.5D, 1.6D, 0.5D);
				Vector3D max = nearbyLoc.add(0.5D, 0.3D, 0.5D);

				if (hasIntersection(start, end, min, max)) {
					if (inSight == null || inSight.getLocation().distanceSquared(playerLoc) > nearbyEntity.getLocation()
							.distanceSquared(playerLoc)) {
						inSight = (Player) nearbyEntity;
						return inSight;
					}
				}
			}
		}
		ChatUtil.sendMessage(player, "&cThe &6Egg &conly works in ratio of &f" + distance + "&c.");
		return inSight;
	}
}
