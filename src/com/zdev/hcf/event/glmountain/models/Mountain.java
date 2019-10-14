package com.zdev.hcf.event.glmountain.models;

import org.bukkit.Material;

public class Mountain {
	private final CuboidRegion region;
	private final Material material;

	private int remainingBlocks;

	public Mountain(CuboidRegion region, Material material) {
		this.region = region;
		this.material = material;
	}

	public void reset() {
		region.getBlocks().forEach(block -> block.setType(material));
		remainingBlocks = region.getBlocks().size();
	}

	public CuboidRegion getRegion() {
		return region;
	}

	public int getRemainingBlocks() {
		return remainingBlocks;
	}

	public void handleBlockBreak() {
		remainingBlocks--;
	}
}
