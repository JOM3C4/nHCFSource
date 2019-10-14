package com.zdev.hcf.event.argument;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.Base;
import com.zdev.hcf.event.EventType;
import com.zdev.hcf.event.faction.ConquestFaction;
import com.zdev.hcf.event.faction.KothFaction;
import com.zdev.hcf.faction.type.Faction;
import com.zdev.hcf.util.command.CommandArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventCreateArgument extends CommandArgument {
	private final Base plugin;

	public EventCreateArgument(final Base plugin) {
		super("create", "Defines a new event", new String[] { "make", "define" });
		this.plugin = plugin;
		this.permission = "command.game.argument." + this.getName();
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName() + " <eventName> <Conquest|KOTH>";
	}

	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (args.length < 3) {
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		Faction faction = this.plugin.getFactionManager().getFaction(args[1]);
		if (faction != null) {
			sender.sendMessage(ChatColor.RED + "There is already a faction named " + args[1] + '.');
			return true;
		}
		final String upperCase = args[2].toUpperCase();
		switch (upperCase) {
		case "CONQUEST": {
			faction = new ConquestFaction(args[1]);
			break;
		}
		case "KOTH": {
			faction = new KothFaction(args[1]);
			break;
		}
		default: {
			sender.sendMessage(this.getUsage(label));
			return true;
		}
		}
		this.plugin.getFactionManager().createFaction(faction, sender);
		sender.sendMessage(
				ChatColor.YELLOW + "Created event faction " + ChatColor.WHITE + faction.getDisplayName(sender)
						+ ChatColor.YELLOW + " with type " + WordUtils.capitalizeFully(args[2]) + '.');
		return true;
	}

	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (args.length != 3) {
			return Collections.emptyList();
		}
		final EventType[] eventTypes = EventType.values();
		final List<String> results = new ArrayList<String>(eventTypes.length);
		for (final EventType eventType : eventTypes) {
			results.add(eventType.name());
		}
		return results;
	}
}
