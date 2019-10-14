package com.zdev.hcf.keyall;

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
import com.zdev.hcf.sale.SaleListener;
import com.zdev.hcf.timer.event.TimerExpireEvent;
import com.zdev.hcf.util.BukkitUtils;
import com.zdev.hcf.util.DurationFormatter;
import com.zdev.hcf.util.JavaUtils;

import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;

public class KeyAllCommand implements CommandExecutor, TabCompleter {

	private final Base plugin;

	public KeyAllCommand(Base plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("command.keyall")) {
			sender.sendMessage(ChatColor.RED + "No permission.");
			return true;
		}

		if (args.length == 0 || args.length > 2) {
			sender.sendMessage("§7§m--------------------------------");
			sender.sendMessage("§b/keyall start <duration> - Starts the key-all timer.");
			sender.sendMessage("§b/keyall end - Ends the current keyall timer.");
			sender.sendMessage("§7§m--------------------------------");
			return true;
		}

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("end")) {
				if (plugin.getTimerManager().getKeyAllTimer().clearCooldown()) {
					Bukkit.broadcastMessage(ChatColor.DARK_AQUA.toString() + ChatColor.STRIKETHROUGH
							+ BukkitUtils.STRAIGHT_LINE_DEFAULT);
					Bukkit.broadcastMessage(ChatColor.AQUA + "The KeyAll has been cancelled!");
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
					sender.sendMessage(ChatColor.RED + "Sale time must last for at least 20 ticks.");
					return true;
				}

				KeyAllTimer keyallTimer = plugin.getTimerManager().getKeyAllTimer();

				if (!keyallTimer.setRemaining(duration, true)) {
					sender.sendMessage(ChatColor.RED + "The KeyAll is already on.");
					return true;
				}

				sender.sendMessage(ChatColor.GREEN + "Started KeyAll timer for "
						+ DurationFormatUtils.formatDurationWords(duration, true, true) + ".");

				Bukkit.broadcastMessage(ChatColor.GRAY + "A KeyAll will " + ChatColor.GRAY + " happen in "
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
