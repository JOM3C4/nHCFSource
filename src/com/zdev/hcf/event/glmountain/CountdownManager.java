package com.zdev.hcf.event.glmountain;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

class CountdownManager extends BukkitRunnable {
	private final MountainHandler mountainHandler;
	private final Map<Integer, String> broadcasts;

	private final int resetInterval;
	private int remainingSeconds;

	CountdownManager(MountainHandler mountainHandler, int resetInterval, Map<Integer, String> broadcasts) {
		this.broadcasts = broadcasts;
		this.mountainHandler = mountainHandler;
		this.resetInterval = resetInterval;
		remainingSeconds = resetInterval;
	}

	@Override
	public void run() {
		String broadcast = broadcasts.get(remainingSeconds);

		if (broadcast != null) {
			Bukkit.broadcastMessage(broadcast);
		}

		if (remainingSeconds == 0) {
			mountainHandler.resetAllMountains();
			remainingSeconds = resetInterval;
			return;
		}

		remainingSeconds--;
	}
}
