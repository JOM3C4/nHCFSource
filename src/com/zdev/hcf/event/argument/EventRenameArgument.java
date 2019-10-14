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

public class EventRenameArgument extends CommandArgument {
	private final Base plugin;

	public EventRenameArgument(final Base plugin) {
		super("rename", "Renames an event");
		this.plugin = plugin;
		this.permission = "command.game.argument." + this.getName();
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName() + " <oldName> <newName>";
	}

	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (args.length < 3) {
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		Faction faction = this.plugin.getFactionManager().getFaction(args[2]);
		if (faction != null) {
			sender.sendMessage(ChatColor.RED + "There is already a faction named " + args[2] + '.');
			return true;
		}
		faction = this.plugin.getFactionManager().getFaction(args[1]);
		if (!(faction instanceof EventFaction)) {
			sender.sendMessage(ChatColor.RED + "There is not an event faction named '" + args[1] + "'.");
			return true;
		}
		final String oldName = faction.getName();
		faction.setName(args[2], sender);
		sender.sendMessage(ChatColor.YELLOW + "Renamed event " + ChatColor.WHITE + oldName + ChatColor.YELLOW + " to "
				+ ChatColor.WHITE + faction.getName() + ChatColor.YELLOW + '.');
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
