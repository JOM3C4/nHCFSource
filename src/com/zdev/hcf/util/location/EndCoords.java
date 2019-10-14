package com.zdev.hcf.util.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import com.zdev.hcf.Base;

public class EndCoords {

	public static Location getExit() {
		FileConfiguration config = Base.getPlugin().getConfig();

		if (config.contains("End.Exit.X")) {
			double x = config.getInt("End.Exit.X");
			double y = config.getInt("End.Exit.Y");
			double z = config.getInt("End.Exit.Z");
			World world = Bukkit.getWorld(config.getString("End.Exit.World"));

			Location location = new Location(world, x, y, z);
			return location;
		} else {
			Location defaultLocation = new Location(Bukkit.getWorld("world"), 0, 80, 200);
			return defaultLocation;
		}
	}

	public static void setExit(String world, Double x, Double y, Double z) {
		FileConfiguration config = Base.getPlugin().getConfig();
		config.set("End.Exit.World", world);
		config.set("End.Exit.X", x);
		config.set("End.Exit.Y", y);
		config.set("End.Exit.Z", z);
		Base.getPlugin().saveConfig();
	}

	public static Location getSpawn() {
		FileConfiguration config = Base.getPlugin().getConfig();

		if (config.contains("End.Spawn.X")) {
			double x = config.getInt("End.Spawn.X");
			double y = config.getInt("End.Spawn.Y");
			double z = config.getInt("End.Spawn.Z");
			World world = Bukkit.getWorld(config.getString("End.Spawn.World"));

			Location location = new Location(world, x, y, z);
			return location;
		} else {
			Location defaultLocation = new Location(Bukkit.getWorld("world_the_end"), 0, 100, 200);
			return defaultLocation;
		}
	}

	public static void setSpawn(String world, Double x, Double y, Double z) {
		FileConfiguration config = Base.getPlugin().getConfig();
		config.set("End.Spawn.World", world);
		config.set("End.Spawn.X", x);
		config.set("End.Spawn.Y", y);
		config.set("End.Spawn.Z", z);
		Base.getPlugin().saveConfig();
	}
}
