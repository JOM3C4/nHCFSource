package com.zdev.hcf.faction.type;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.zdev.hcf.ConfigurationService;
import com.zdev.hcf.faction.claim.Claim;

public class SpawnFaction extends ClaimableFaction implements ConfigurationSerializable {
	public SpawnFaction() {
		super("Spawn");
		this.safezone = true;
		for (World world : Bukkit.getWorlds()) {
			World.Environment environment = world.getEnvironment();
			if (environment != World.Environment.THE_END) {
				double radius = ((Double) ConfigurationService.SPAWN_RADIUS_MAP.get(world.getEnvironment()))
						.doubleValue();
				addClaim(new Claim(this, new Location(world, radius, 0.0D, radius),
						new Location(world, -radius, world.getMaxHeight(), -radius)), null);
			}
		}
	}

	public SpawnFaction(Map<String, Object> map) {
		super(map);
	}

	public boolean isDeathban() {
		return false;
	}
}
