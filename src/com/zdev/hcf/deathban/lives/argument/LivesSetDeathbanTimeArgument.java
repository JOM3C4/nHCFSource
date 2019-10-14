package com.zdev.hcf.deathban.lives.argument;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.util.JavaUtils;
import com.zdev.hcf.util.command.CommandArgument;

public class LivesSetDeathbanTimeArgument extends CommandArgument {
	public LivesSetDeathbanTimeArgument() {
		super("setdeathbantime", "Sets the base deathban time");
		this.permission = ("hcf.command.lives.argument." + getName());
	}

	public String getUsage(String label) {
		return '/' + label + ' ' + getName() + " <time>";
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
			return true;
		}
		long duration = JavaUtils.parse(args[1]);
		if (duration == -1L) {
			sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
			return true;
		}
		com.zdev.hcf.ConfigurationService.DEFAULT_DEATHBAN_DURATION = 7200000;
		Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Base death-ban time set to "
				+ DurationFormatUtils.formatDurationWords(duration, true, true) + " (not including multipliers, etc).");
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return Collections.emptyList();
	}
}
