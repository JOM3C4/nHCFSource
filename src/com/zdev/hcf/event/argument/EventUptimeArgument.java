package com.zdev.hcf.event.argument;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.Base;
import com.zdev.hcf.DateTimeFormats;
import com.zdev.hcf.event.EventTimer;
import com.zdev.hcf.event.faction.EventFaction;
import com.zdev.hcf.util.command.CommandArgument;

public class EventUptimeArgument extends CommandArgument {
	private final Base plugin;

	public EventUptimeArgument(final Base plugin) {
		super("uptime", "Check the uptime of an event");
		this.plugin = plugin;
		this.permission = "command.game.argument." + this.getName();
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName();
	}

	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		final EventTimer eventTimer = this.plugin.getTimerManager().eventTimer;
		if (eventTimer.getRemaining() <= 0L) {
			sender.sendMessage(ChatColor.RED + "There is not a running event.");
			return true;
		}
		final EventFaction eventFaction = eventTimer.getEventFaction();
		sender.sendMessage(ChatColor.YELLOW + "Up-time of " + eventTimer.getName() + " timer"
				+ ((eventFaction == null) ? ""
						: (": " + ChatColor.BLUE + '(' + eventFaction.getDisplayName(sender) + ChatColor.BLUE + ')'))
				+ ChatColor.YELLOW + " is " + ChatColor.GRAY
				+ DurationFormatUtils.formatDurationWords(eventTimer.getUptime(), true, true) + ChatColor.YELLOW
				+ ", started at " + ChatColor.GOLD
				+ DateTimeFormats.HR_MIN_AMPM_TIMEZONE.format(eventTimer.getStartStamp()) + ChatColor.YELLOW + '.');
		return true;
	}
}
