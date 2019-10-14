package com.zdev.hcf.timer.event;

import com.google.common.base.Optional;
import com.zdev.hcf.timer.PlayerTimer;
import com.zdev.hcf.timer.Timer;

import java.util.UUID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TimerClearEvent extends Event {
	public TimerClearEvent(Timer timer) {
		this.userUUID = Optional.absent();
		this.timer = timer;
	}

	public TimerClearEvent(UUID userUUID, PlayerTimer timer) {
		this.userUUID = Optional.of(userUUID);
		this.timer = timer;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Optional<UUID> getUserUUID() {
		return this.userUUID;
	}

	public Timer getTimer() {
		return this.timer;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	private static final HandlerList handlers = new HandlerList();
	private final Optional<UUID> userUUID;
	private final Timer timer;
}
