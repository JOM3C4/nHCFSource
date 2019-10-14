package com.zdev.hcf.event.glmountain;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import com.zdev.hcf.event.glmountain.models.CuboidRegion;
import com.zdev.hcf.event.glmountain.models.Mountain;
import com.zdev.hcf.util.PersistableLocation;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MountainHandler implements Listener {

	private final Plugin plugin;

	private final File file;
	private final FileConfiguration config;

	private final Set<Mountain> mountains = new HashSet<>();
	private final Map<Integer, String> broadcasts;

	public MountainHandler(Plugin plugin, Map<Integer, String> broadcasts) {
		this.broadcasts = broadcasts;
		this.plugin = plugin;

		file = new File(plugin.getDataFolder(), "mountains.yml");

		if (!file.exists() || file.isDirectory()) {
			file.getParentFile().mkdirs();

			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				plugin.getLogger().severe("Failed to create mountains.yml!");
			}
		}

		config = YamlConfiguration.loadConfiguration(file);

		for (String key : config.getConfigurationSection("").getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection(key);

			PersistableLocation minimumPoint = (PersistableLocation) section.get("minimum-point");
			PersistableLocation maximumPoint = (PersistableLocation) section.get("maximum-point");

			Material material = Material.valueOf(section.getString("material"));
			Mountain mountain = new Mountain(new CuboidRegion(minimumPoint.getWorld(),
					minimumPoint.getLocation().toVector(), maximumPoint.getLocation().toVector()), material);

			mountains.add(mountain);
		}

		resetAllMountains();
	}

	public int getPercentage() {
		double totalBlocks = 0;
		double remainingBlocks = 0;

		for (Mountain mountain : mountains) {
			totalBlocks += mountain.getRegion().getBlocks().size();
			remainingBlocks += mountain.getRemainingBlocks();
		}

		return (int) (remainingBlocks / totalBlocks * 100);
	}

	public void resetAllMountains() {
		mountains.forEach(Mountain::reset);
	}

	public void createNewMountain(Location minimumPoint, Location maximumPoint, Material mountainMaterial) {
		mountains.add(new Mountain(
				new CuboidRegion(minimumPoint.getWorld(), minimumPoint.toVector(), maximumPoint.toVector()),
				mountainMaterial));

		ConfigurationSection section = new MemoryConfiguration();

		section.set("minimum-point", new PersistableLocation(minimumPoint));
		section.set("maximum-point", new PersistableLocation(maximumPoint));
		section.set("material", mountainMaterial.toString());

		config.set(Integer.toString(config.getConfigurationSection("").getKeys(false).size()), section);
		saveToDisk();
	}

	private void saveToDisk() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			plugin.getLogger().severe("Failed to save mountains.yml to disk! Changes will not be saved!");
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		for (Mountain mountain : mountains) {
			if (mountain.getRegion().isInAABB(event.getBlock().getLocation())) {
				mountain.handleBlockBreak();

				String broadcast = broadcasts.get(getPercentage());

				if (broadcast != null) {
					Bukkit.broadcastMessage(broadcast);
				}

				return;
			}
		}
	}
}
