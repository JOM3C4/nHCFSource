package com.zdev.hcf.reboot;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.zdev.hcf.Base;
import com.zdev.hcf.reboot.RebootListener;
import com.zdev.hcf.timer.event.TimerExpireEvent;
import com.zdev.hcf.util.BukkitUtils;
import com.zdev.hcf.util.DurationFormatter;
import com.zdev.hcf.util.JavaUtils;

import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;

public class RebootCommand implements CommandExecutor, TabCompleter {

	private final Base plugin;

	public RebootCommand(Base plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("command.reboot")) {
			sender.sendMessage(ChatColor.RED + "No permission.");
			return true;
		}

		if (args.length == 0 || args.length > 2) {
			sender.sendMessage("§9§m--------------------------------");
			sender.sendMessage("§c/reboot start <duration> - Starts the reboot timer.");
			sender.sendMessage("§c/reboot end - Ends the current reboot timer.");
			sender.sendMessage("§9§m--------------------------------");
			return true;
		}

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("end")) {
				if (plugin.getTimerManager().getRebootTimer().clearCooldown()) {
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
					sender.sendMessage(ChatColor.RED + "Reboot time must last for at least 20 ticks.");
					return true;
				}

				RebootTimer rebootTimer = plugin.getTimerManager().getRebootTimer();

				if (!rebootTimer.setRemaining(duration, true)) {
					sender.sendMessage(ChatColor.RED + "Server is already restarting!");
					return true;
				}

				sender.sendMessage(ChatColor.RED + "Started restart timer for "
						+ DurationFormatUtils.formatDurationWords(duration, true, true) + ".");

				Bukkit.broadcastMessage(ChatColor.GRAY + "The server is " + ChatColor.DARK_RED.toString()
						+ ChatColor.BOLD + "rebooting " + ChatColor.GRAY + "in "
						+ DurationFormatUtils.formatDurationWords(duration, true, true) + ".");
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
