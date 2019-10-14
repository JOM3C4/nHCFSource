package com.zdev.hcf.serializable;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import lombok.Getter;

public class SerializableLocation implements ConfigurationSerializable {

	private final int x, y, z;

	private final float yaw, pitch;

	private World world;

	private Location location;

	public SerializableLocation(Location location) {
		this.x = location.getBlockX();
		this.y = location.getBlockY();
		this.z = location.getBlockZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
		this.world = location.getWorld();
		this.location = location;
	}

	public SerializableLocation(Map<String, Object> map) {
		this.x = (int) map.get("x");
		this.y = (int) map.get("y");
		this.z = (int) map.get("z");
		this.yaw = Float.valueOf((String) map.get("yaw"));
		this.pitch = Float.valueOf((String) map.get("pitch"));
		this.world = Bukkit.getWorld((String) map.get("world"));
		this.location = new Location(world, x, y, z, yaw, pitch);
	}

	public static SerializableLocation deserialize(Map<String, Object> map) {
		return new SerializableLocation(map);
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();

		map.put("x", this.x);
		map.put("y", this.y);
		map.put("z", this.z);
		map.put("yaw", String.valueOf(this.yaw));
		map.put("pitch", String.valueOf(this.pitch));

		map.put("world", world.getName());

		return map;
	}

	public Location getLocation() {
		return null;
	}

}
