package com.zdev.hcf.event.argument;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.Base;
import com.zdev.hcf.event.EventTimer;
import com.zdev.hcf.event.EventType;
import com.zdev.hcf.event.tracker.KothTracker;
import com.zdev.hcf.faction.type.Faction;
import com.zdev.hcf.util.command.CommandArgument;

public class EventCancelArgument extends CommandArgument {
	private final Base plugin;

	public EventCancelArgument(Base plugin) {
		super("cancel", "Cancels a running event", new String[] { "stop", "end" });
		this.plugin = plugin;
		this.permission = ("hcf.command.event.argument." + getName());
	}

	public String getUsage(String label) {
		return '/' + label + ' ' + getName();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		EventTimer eventTimer = this.plugin.getTimerManager().eventTimer;
		Faction eventFaction = eventTimer.getEventFaction();
		if (!eventTimer.clearCooldown()) {
			sender.sendMessage(ChatColor.RED + "There is not a running event.");
			return true;
		}
		return true;
	}
}
