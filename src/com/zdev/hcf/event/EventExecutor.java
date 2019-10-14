package com.zdev.hcf.event;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.Base;
import com.zdev.hcf.event.argument.EventCancelArgument;
import com.zdev.hcf.event.argument.EventCreateArgument;
import com.zdev.hcf.event.argument.EventDeleteArgument;
import com.zdev.hcf.event.argument.EventRenameArgument;
import com.zdev.hcf.event.argument.EventSetAreaArgument;
import com.zdev.hcf.event.argument.EventSetCapzoneArgument;
import com.zdev.hcf.event.argument.EventStartArgument;
import com.zdev.hcf.event.argument.EventUptimeArgument;
import com.zdev.hcf.util.BukkitUtils;
import com.zdev.hcf.util.command.ArgumentExecutor;
import com.zdev.hcf.util.command.CommandArgument;

public class EventExecutor extends ArgumentExecutor {
	public EventExecutor(Base plugin) {
		super("event");
		addArgument(new EventCancelArgument(plugin));
		addArgument(new EventCreateArgument(plugin));
		addArgument(new EventDeleteArgument(plugin));
		addArgument(new EventRenameArgument(plugin));
		addArgument(new EventSetAreaArgument(plugin));
		addArgument(new EventSetCapzoneArgument(plugin));
		addArgument(new EventStartArgument(plugin));
		addArgument(new EventUptimeArgument(plugin));
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			if (sender.hasPermission("event.admin")) {
				sender.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
				sender.sendMessage(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Event Help");
				sender.sendMessage(ChatColor.AQUA + " /event cancel" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY
						+ "Cancels a running event.");
				sender.sendMessage(ChatColor.AQUA + " /event create" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY
						+ "Defines a new event.");
				sender.sendMessage(ChatColor.AQUA + " /event delete" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY
						+ "Deletes an event.");
				sender.sendMessage(ChatColor.AQUA + " /event setarea" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY
						+ "Sets the area of an event.");
				sender.sendMessage(ChatColor.AQUA + " /event setcapzone" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY
						+ "Sets the capzone of an event.");
				sender.sendMessage(ChatColor.AQUA + " /event start" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY
						+ "Start an event.");
				sender.sendMessage(ChatColor.AQUA + " /event rename" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY
						+ "Renames an event.");
				sender.sendMessage(ChatColor.AQUA + " /event uptime" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY
						+ "Check the uptime of an event.");
				sender.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
			}
			return true;
		}
		CommandArgument argument2 = getArgument(args[0]);
		String permission2 = argument2 == null ? null : argument2.getPermission();
		if ((argument2 == null) || ((permission2 != null) && (!sender.hasPermission(permission2)))) {
			sender.sendMessage(ChatColor.RED + "Command not found");
			return true;
		}
		argument2.onCommand(sender, command, label, args);
		return true;
	}
}
