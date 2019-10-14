package com.zdev.hcf.inventory.implementation.runnable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockChangeRunnable extends BukkitRunnable {

	private final int maxPerTick;
	private int x, y, z, maxY, blocksPlaced;

	private final Location min, max;

	private final Material material;
	private final BlockOperation operation;

	public BlockChangeRunnable(BlockOperation operation, Material material, int minY, Location min, Location max,
			int maxPerTick) {
		this.max = max;
		this.min = min;
		this.operation = operation;
		this.maxPerTick = maxPerTick;
		this.x = min.getBlockX();
		this.y = minY;
		this.z = min.getBlockZ();
		this.maxY = 100;
		this.material = material;

		System.out.println(y);
	}

	@Override
	public void run() {
		Block block = null;
		this.blocksPlaced = 0;

		switch (operation) {
		case X:
			for (; y <= maxY; y++) {
				for (; x <= max.getBlockX(); x++) {
					if (blocksPlaced >= maxPerTick)
						return;

					block = new Location(min.getWorld(), x, y, z).getBlock();

					if (!block.getChunk().isLoaded()) {
						block.getChunk().load();
						return;
					}

					if (block.getType() != material) {
						block.setType(material);
						blocksPlaced++;
					}

					block = new Location(min.getWorld(), x, y, max.getBlockZ()).getBlock();

					if (!block.getChunk().isLoaded()) {
						block.getChunk().load();
						return;
					}

					if (block.getType() != material) {
						block.setType(material);
						blocksPlaced++;
					}
				}
				x = min.getBlockX();
			}
			this.cancel();
			break;
		case Z:
			for (; y <= maxY; y++) {
				for (; z <= max.getBlockZ(); z++) {
					if (blocksPlaced >= maxPerTick)
						return;

					block = new Location(min.getWorld(), x, y, z).getBlock();

					if (!block.getChunk().isLoaded()) {
						block.getChunk().load();
						return;
					}

					if (block.getType() != material) {
						block.setType(material);
						blocksPlaced++;
					}

					block = new Location(min.getWorld(), max.getBlockX(), y, z).getBlock();

					if (!block.getChunk().isLoaded()) {
						block.getChunk().load();
						return;
					}

					if (block.getType() != material) {
						block.setType(material);
						blocksPlaced++;
					}
				}
				z = min.getBlockZ();
				if (y == maxY) {
					for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
						for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
							block = min.getWorld().getBlockAt(x, y, z);
							block.setType(material);
						}
					}
				}

			}
			this.cancel();
			break;
		default:
			break;
		}
	}

	public enum BlockOperation {
		X, Z;
	}

}
