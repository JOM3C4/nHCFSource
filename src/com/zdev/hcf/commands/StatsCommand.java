package com.zdev.hcf.commands;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class StatsCommand implements CommandExecutor, Listener {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		if ((cmd.getName().equalsIgnoreCase("stats")) && ((sender instanceof Player)) && (args.length > 1)) {
			sender.sendMessage("§cUsage: /stats <player>");
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage("§cUsage: /stats <player>");
			return true;
		}
		Player target = Bukkit.getServer().getPlayer(args[0]);
		if ((args.length == 1) && (target == null)) {
			player.sendMessage("§cPlayer not found");
			return true;
		}

		sender.sendMessage("§7§m--------------------------------------------------");
		sender.sendMessage("                     §eStatistics of §6" + target.getDisplayName());
		sender.sendMessage("§aKills(s): §7" + target.getStatistic(Statistic.PLAYER_KILLS));
		sender.sendMessage("§4Death(s): §7" + target.getStatistic(Statistic.DEATHS));
		sender.sendMessage("§7§m--------------------------------------------------");

		return false;
	}
}