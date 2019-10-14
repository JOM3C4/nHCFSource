package com.zdev.hcf.event.king;

import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.util.BukkitUtils;

import java.util.UUID;
import java.util.ArrayList;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;

public class KingCommand implements CommandExecutor, TabCompleter {
	public static ArrayList<UUID> play;
	private final Base plugin;
	public static Player player;
	public static String kingName;
	public static String kingPrize;

	static {
		KingCommand.play = new ArrayList<UUID>();
		KingCommand.kingName = "";
		KingCommand.kingPrize = "";
	}

	public KingCommand(final Base plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (!sender.hasPermission("command.kingevent")) {
			sender.sendMessage(ChatColor.RED + "No permission.");
			return true;
		}
		if (args.length == 0 || args.length > 3) {
			sender.sendMessage("§7§m--------------------------------");
			sender.sendMessage("§4§lKing Event");
			sender.sendMessage("");
			sender.sendMessage("§4/kingevent start <player>§7 - §fStarts the King Event.");
			sender.sendMessage("§4/kingevent end §7- §fEnds the current king event.");
			sender.sendMessage("§4/kingevent prize <prize> §7- §fSets the prize for the current king event.");
			sender.sendMessage("§7§m--------------------------------");
			return true;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("end")) {
				KingCommand.player = null;
				return true;
			}
			sender.sendMessage(ChatColor.RED + "Unknown sub-command!");
			return true;
		} else {
			if (args.length == 2 && args[0].equalsIgnoreCase("prize")) {
				KingCommand.kingPrize = args[1].replaceAll("_", " ");
				sender.sendMessage(ChatColor.GREEN + "You have successfully set the prize to " + KingCommand.kingPrize);
				return true;
			}
			if (args.length == 2 && args[0].equalsIgnoreCase("prize")) {
				KingCommand.kingPrize = args[1].replaceAll("_", " ");
				sender.sendMessage(ChatColor.GREEN + "You have successfully set the prize to " + KingCommand.kingPrize);
				return true;
			}
			if (args.length == 2) {
				if (!args[0].equalsIgnoreCase("start")) {
					sender.sendMessage(ChatColor.RED + "Unknown sub-command!");
					return true;
				}
				final Player p = Bukkit.getPlayer(args[1]);
				KingCommand.kingName = p.getName();
				final Player player1 = Bukkit.getPlayer(KingCommand.kingName);
				sender.sendMessage(ChatColor.GREEN + "You have successfully started the king event!");
				Bukkit.broadcastMessage(String.valueOf(String.valueOf(ChatColor.GRAY.toString()))
						+ ChatColor.STRIKETHROUGH + BukkitUtils.STRAIGHT_LINE_DEFAULT);
				Bukkit.broadcastMessage(
						String.valueOf(String.valueOf(ChatColor.GRAY.toString())) + ChatColor.STRIKETHROUGH);
				Bukkit.broadcastMessage(
						String.valueOf(String.valueOf(ChatColor.GOLD.toString())) + ChatColor.BOLD + "King Event");
				Bukkit.broadcastMessage(
						String.valueOf(String.valueOf(ChatColor.GRAY.toString())) + ChatColor.STRIKETHROUGH);
				Bukkit.broadcastMessage(
						ChatColor.GOLD + " »§e King" + ChatColor.GRAY + ": " + ChatColor.WHITE + KingCommand.kingName);
				Bukkit.broadcastMessage(ChatColor.GOLD + " »§e Location" + ChatColor.GRAY + ": " + ChatColor.WHITE + "x"
						+ player1.getLocation().getBlockX() + ", y" + player1.getLocation().getBlockY() + ", z"
						+ player1.getLocation().getBlockZ());
				Bukkit.broadcastMessage(ChatColor.GOLD + " »§e Starting Health" + ChatColor.GRAY + ": "
						+ ChatColor.WHITE + player1.getHealthScale());
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(String.valueOf(String.valueOf(ChatColor.GRAY.toString()))
						+ ChatColor.STRIKETHROUGH + BukkitUtils.STRAIGHT_LINE_DEFAULT);
				if (p == null) {
					sender.sendMessage(ChatColor.RED + "That player is not online.");
					return true;
				}
				KingCommand.player = p;
			}
			return true;
		}
	}

	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		return Collections.emptyList();
	}
}
