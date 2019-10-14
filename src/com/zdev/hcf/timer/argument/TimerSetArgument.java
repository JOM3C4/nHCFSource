package com.zdev.hcf.timer.argument;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.timer.PlayerTimer;
import com.zdev.hcf.timer.Timer;
import com.zdev.hcf.util.JavaUtils;
import com.zdev.hcf.util.command.CommandArgument;

public class TimerSetArgument extends CommandArgument {
	public TimerSetArgument(Base plugin) {
		super("set", "Set remaining timer time");
		this.plugin = plugin;
		this.permission = ("hcf.command.timer.argument." + getName());
	}

	public String getUsage(String label) {
		return '/' + label + ' ' + getName() + " <timerName> <all|playerName> <remaining>";
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 4) {
			sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
			return true;
		}
		long duration = JavaUtils.parse(args[3]);
		if (duration == -1L) {
			sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
			return true;
		}
		PlayerTimer playerTimer = null;
		for (Timer timer : this.plugin.getTimerManager().getTimers()) {
			if (((timer instanceof PlayerTimer))
					&& (WHITESPACE_TRIMMER.matcher(timer.getName()).replaceAll("").equalsIgnoreCase(args[1]))) {
				playerTimer = (PlayerTimer) timer;
				break;
			}
		}
		if (playerTimer == null) {
			sender.sendMessage(ChatColor.RED + "Timer '" + args[1] + "' not found.");
			return true;
		}
		if (args[2].equalsIgnoreCase("all")) {
			Player[] arrayOfPlayer;
			int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
			for (int i = 0; i < j; i++) {
				Player player = arrayOfPlayer[i];
				playerTimer.setCooldown(player, player.getUniqueId(), duration, true);
			}
			sender.sendMessage(ChatColor.BLUE + "Set timer " + playerTimer.getName() + " for all to "
					+ DurationFormatUtils.formatDurationWords(duration, true, true) + '.');
		} else {
			OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
			Player targetPlayer = null;
			if ((target == null) || (((sender instanceof Player)) && ((targetPlayer = target.getPlayer()) != null)
					&& (!((Player) sender).canSee(targetPlayer)))) {
				sender.sendMessage(
						ChatColor.GOLD + "Player '" + ChatColor.WHITE + args[1] + ChatColor.GOLD + "' not found.");
				return true;
			}
			playerTimer.setCooldown(targetPlayer, target.getUniqueId(), duration, true);
			sender.sendMessage(ChatColor.BLUE + "Set timer " + playerTimer.getName() + " duration to "
					+ DurationFormatUtils.formatDurationWords(duration, true, true) + " for " + target.getName() + '.');
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
		List<String> result = new ArrayList<String>();
		if (arguments.length == 2) {
			for (Timer timer : this.plugin.getTimerManager().getTimers()) {
				if ((timer instanceof PlayerTimer)) {
					result.add(WHITESPACE_TRIMMER.matcher(timer.getName()).replaceAll(""));
				}
			}
		} else if (arguments.length == 3) {
			result.add("ALL");
			Player player = (sender instanceof Player) ? (Player) sender : null;
			for (Player target : Bukkit.getOnlinePlayers()) {
				if ((player == null) || (player.canSee(target))) {
					result.add(target.getName());
				}
			}
		}
		return result;
	}

	private static final Pattern WHITESPACE_TRIMMER = Pattern.compile("\\s");
	private final Base plugin;
}
