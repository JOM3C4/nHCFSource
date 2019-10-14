package com.zdev.hcf.event.tracker;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.DateTimeFormats;
import com.zdev.hcf.event.CaptureZone;
import com.zdev.hcf.event.EventTimer;
import com.zdev.hcf.event.EventType;
import com.zdev.hcf.event.faction.EventFaction;
import com.zdev.hcf.event.faction.KothFaction;

public class KothTracker implements EventTracker {
	private final Base plugin;

	public KothTracker(Base plugin) {
		this.plugin = plugin;
	}

	public EventType getEventType() {
		return EventType.KOTH;
	}

	public void tick(EventTimer eventTimer, EventFaction eventFaction) {
		CaptureZone captureZone = ((KothFaction) eventFaction).getCaptureZone();
		long remainingMillis = captureZone.getRemainingCaptureMillis();
		if (remainingMillis <= 0L) {
			this.plugin.getTimerManager().eventTimer.handleWinner(captureZone.getCappingPlayer());
			eventTimer.clearCooldown();
			return;
		}
		if (remainingMillis == captureZone.getDefaultCaptureMillis()) {
			return;
		}
		int remainingSeconds = (int) (remainingMillis / 1000L);
		if ((remainingSeconds > 0) && (remainingSeconds % 30 == 0)) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + "KingOfTheHill" + "] " + ChatColor.YELLOW
					+ captureZone.getDisplayName() + ChatColor.GOLD + " is trying to be controlled.");
			Bukkit.broadcastMessage(ChatColor.GOLD + " - Time Left: " + ChatColor.BLUE
					+ DateTimeFormats.KOTH_FORMAT.format(remainingMillis));
		}
	}

	public void onContest(EventFaction eventFaction, EventTimer eventTimer) {
		Bukkit.broadcastMessage(ChatColor.GOLD + "[" + "KingOfTheHill" + "] " + ChatColor.YELLOW
				+ eventFaction.getName() + ChatColor.GOLD + " can now be contested. ");
		Bukkit.broadcastMessage(ChatColor.GOLD + " - Time Left: " + ChatColor.BLUE
				+ DateTimeFormats.KOTH_FORMAT.format(eventTimer.getRemaining()));
	}

	public boolean onControlTake(Player player, CaptureZone captureZone) {
		player.sendMessage(ChatColor.GOLD + "You are now in control of " + ChatColor.YELLOW
				+ captureZone.getDisplayName() + ChatColor.GOLD + '.');
		return true;
	}

	public boolean onControlLoss(Player player, CaptureZone captureZone, EventFaction eventFaction) {
		player.sendMessage(ChatColor.GOLD + "You are no longer in control of " + ChatColor.YELLOW
				+ captureZone.getDisplayName() + ChatColor.GOLD + '.');
		long remainingMillis = captureZone.getRemainingCaptureMillis();
		if ((remainingMillis > 0L)
				&& (captureZone.getDefaultCaptureMillis() - remainingMillis > MINIMUM_CONTROL_TIME_ANNOUNCE)) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "[" + "KingOfTheHill" + "] " + ChatColor.YELLOW + player.getName()
					+ ChatColor.GOLD + " has lost control of " + ChatColor.YELLOW + captureZone.getDisplayName()
					+ ChatColor.GOLD + '.');
			Bukkit.broadcastMessage(ChatColor.GOLD + " - Time Left: " + ChatColor.BLUE
					+ DateTimeFormats.KOTH_FORMAT.format(captureZone.getRemainingCaptureMillis()));
		}
		return true;
	}

	private static final long MINIMUM_CONTROL_TIME_ANNOUNCE = TimeUnit.SECONDS.toMillis(25L);
	public static final long DEFAULT_CAP_MILLIS = TimeUnit.MINUTES.toMillis(5L);

	public void stopTiming() {
	}
}
