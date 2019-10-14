package com.zdev.hcf.event.glmountain.models;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class CuboidRegion {
	private final World world;
	private final Vector minimumPoint;
	private final Vector maximumPoint;

	private final Set<Block> blocks = new HashSet<Block>();

	public CuboidRegion(World world, Vector minimumPoint, Vector maximumPoint) {
		this.world = world;
		this.minimumPoint = minimumPoint;
		this.maximumPoint = maximumPoint;

		for (int x = minimumPoint.getBlockX(); x <= maximumPoint.getBlockX(); x++) {
			for (int y = minimumPoint.getBlockY(); y <= maximumPoint.getBlockY(); y++) {
				for (int z = minimumPoint.getBlockZ(); z <= maximumPoint.getBlockZ(); z++) {
					blocks.add(world.getBlockAt(x, y, z));
				}
			}
		}
	}

	public Set<Block> getBlocks() {
		return blocks;
	}

	public boolean isInAABB(Location location) {
		return location.getWorld() == world && location.toVector().isInAABB(minimumPoint, maximumPoint);
	}
}
