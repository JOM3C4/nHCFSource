package com.zdev.hcf.tripplekeys;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.zdev.hcf.Base;
import com.zdev.hcf.ConfigurationService;
import com.zdev.hcf.timer.event.TimerExpireEvent;
import com.zdev.hcf.util.BukkitUtils;
import com.zdev.hcf.util.DurationFormatter;
import com.zdev.hcf.util.JavaUtils;

import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;

public class TrippleKeyCommand implements CommandExecutor, TabCompleter {

	private final Base plugin;

	public TrippleKeyCommand(Base plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("command.tkey")) {
			sender.sendMessage(ChatColor.RED + "No permission.");
			return true;
		}

		if (args.length == 0 || args.length > 2) {
			sender.sendMessage("�7�m--------------------------------");
			sender.sendMessage("�b/tkey start <duration> - Starts the TripleKey timer.");
			sender.sendMessage("�b/tkey end - Ends the current TripleKey timer.");
			sender.sendMessage("�7�m--------------------------------");
			return true;
		}

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("end")) {
				if (plugin.getTimerManager().getTrippleKeyTimer().clearCooldown()) {
					Bukkit.broadcastMessage(ChatColor.DARK_AQUA.toString() + ChatColor.STRIKETHROUGH
							+ BukkitUtils.STRAIGHT_LINE_DEFAULT);
					Bukkit.broadcastMessage(ChatColor.AQUA + "The store-wide TripleKeys has been cancelled!");
					Bukkit.broadcastMessage(ChatColor.DARK_AQUA.toString() + ChatColor.STRIKETHROUGH
							+ BukkitUtils.STRAIGHT_LINE_DEFAULT);
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Unknown sub-command!");
				return true;
			}
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("start")) {
				long duration = JavaUtils.parse(args[1]);

				if (duration == -1L) {
					sender.sendMessage(ChatColor.RED + "'" + args[0] + "' is an invalid duration.");
					return true;
				}

				if (duration < 1000L) {
					sender.sendMessage(ChatColor.RED + "TripleKey time must last for at least 20 ticks.");
					return true;
				}

				TrippleKeyTimer saleTimer = plugin.getTimerManager().getTrippleKeyTimer();

				if (!saleTimer.setRemaining(duration, true)) {
					sender.sendMessage(ChatColor.RED + "The TripleKey is already on.");
					return true;
				}

				sender.sendMessage(ChatColor.GREEN + "Started TripleKey timer for "
						+ DurationFormatUtils.formatDurationWords(duration, true, true) + ".");

				Bukkit.broadcastMessage(ChatColor.GRAY + "The store-wide " + ChatColor.DARK_RED.toString()
						+ ChatColor.BOLD + "TripleKey " + ChatColor.GRAY + "has started at " + ChatColor.DARK_AQUA
						+ ConfigurationService.DONATE_URL + ChatColor.GRAY + " for "
						+ DurationFormatUtils.formatDurationWords(duration, true, true) + ChatColor.GRAY + ".");
			} else {
				sender.sendMessage(ChatColor.RED + "Unknown sub-command!");
				return true;
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return Collections.emptyList();
	}
}
