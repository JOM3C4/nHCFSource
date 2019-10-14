package com.zdev.hcf.event.tracker;

import org.bukkit.entity.Player;

import com.zdev.hcf.event.CaptureZone;
import com.zdev.hcf.event.EventTimer;
import com.zdev.hcf.event.EventType;
import com.zdev.hcf.event.faction.EventFaction;

public abstract interface EventTracker {
	public abstract EventType getEventType();

	public abstract void tick(EventTimer paramEventTimer, EventFaction paramEventFaction);

	public abstract void onContest(EventFaction paramEventFaction, EventTimer paramEventTimer);

	public abstract boolean onControlTake(Player paramPlayer, CaptureZone paramCaptureZone);

	public abstract boolean onControlLoss(Player paramPlayer, CaptureZone paramCaptureZone,
			EventFaction paramEventFaction);

	public abstract void stopTiming();
}
