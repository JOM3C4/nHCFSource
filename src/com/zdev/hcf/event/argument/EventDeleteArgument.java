package com.zdev.hcf.event.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.Base;
import com.zdev.hcf.event.faction.EventFaction;
import com.zdev.hcf.faction.type.Faction;
import com.zdev.hcf.util.command.CommandArgument;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EventDeleteArgument extends CommandArgument {
	private final Base plugin;

	public EventDeleteArgument(final Base plugin) {
		super("delete", "Deletes an event");
		this.plugin = plugin;
		this.aliases = new String[] { "remove", "del" };
		this.permission = "command.game.argument." + this.getName();
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName() + " <eventName>";
	}

	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		final Faction faction = this.plugin.getFactionManager().getFaction(args[1]);
		if (!(faction instanceof EventFaction)) {
			sender.sendMessage(ChatColor.RED + "There is not an event faction named '" + args[1] + "'.");
			return true;
		}

		if (this.plugin.getFactionManager().removeFaction(faction, sender)) {
			sender.sendMessage(ChatColor.YELLOW + "Deleted event faction " + ChatColor.AQUA
					+ faction.getDisplayName(sender) + ChatColor.YELLOW + '.');
		}
		return true;
	}

	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (args.length != 2) {
			return Collections.emptyList();
		}
		return this.plugin.getFactionManager().getFactions().stream().filter(faction -> faction instanceof EventFaction)
				.map(Faction::getName).collect(Collectors.toList());
	}
}
