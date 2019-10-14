package com.zdev.hcf.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Squid;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.zdev.hcf.Base;
import com.zdev.hcf.ConfigurationService;

/**
 * Listener that limits the amount of entities in one chunk.
 */
//TODO Fix to work with mob stacking
public class EntityLimitListener implements Listener {

	private static final int MAX_CHUNK_GENERATED_ENTITIES = 25;
	private static final int MAX_NATURAL_CHUNK_ENTITIES = 25;

	private final Base plugin;

	public EntityLimitListener(Base plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if ((ConfigurationService.KIT_MAP) == true) {
			return;
		}

		Entity entity = event.getEntity();
		if (entity instanceof Squid) {
			event.setCancelled(true);
			return;
		}

		if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) { // allow slimes to always split
			switch (event.getSpawnReason()) {
			case NATURAL:
				if (event.getLocation().getChunk().getEntities().length > MAX_NATURAL_CHUNK_ENTITIES) {
					event.setCancelled(true);
				}
				break;
			case CHUNK_GEN:
				if (event.getLocation().getChunk().getEntities().length > MAX_CHUNK_GENERATED_ENTITIES) {
					event.setCancelled(true);
				}
				break;
			default:
				break;
			}
		}
	}
}
