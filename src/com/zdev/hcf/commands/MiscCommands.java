package com.zdev.hcf.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class MiscCommands implements CommandExecutor {
	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel,
			final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You cannot do this in console");
			return true;
		}
		final Player p = (Player) sender;
		p.getLocation();
		p.getInventory();
		String perm = ChatColor.translateAlternateColorCodes('&', "&cNo.");
		perm = perm.replaceAll("&", "§");
		if (cmd.getName().equalsIgnoreCase("drop")) {
			if (!p.hasPermission("drop.yes")) {
				p.sendMessage(perm);
				return true;
			}
			if (args.length == 0) {
				p.sendMessage(ChatColor.GRAY + "/drop <help, types, list>");
				return true;
			}
			if (args[0].equalsIgnoreCase("help") | args[0].equalsIgnoreCase("helpme")
					| args[0].equalsIgnoreCase("help")) {
				p.sendMessage(ChatColor.RED + "\u2727 " + ChatColor.BLUE + ChatColor.BOLD
						+ "                     Help Menu                            " + ChatColor.RED + " \u2727");
				p.sendMessage(ChatColor.RED + "[" + ChatColor.GRAY + "+" + ChatColor.RED + "]" + ChatColor.GRAY
						+ " /drop is a command that allows the user to remotely drop a target for a minecraft error message.");
				p.sendMessage(ChatColor.RED + "[" + ChatColor.GRAY + "+" + ChatColor.RED + "]" + ChatColor.GRAY
						+ " There are many diffrent options to choose from.");
				p.sendMessage(ChatColor.GRAY + "- " + ChatColor.BLUE
						+ "To drop a player do §7/drop §9<§7player§9> <§7error§9>");
				p.sendMessage(ChatColor.GRAY + "- " + ChatColor.BLUE + "To list the types of error's do §7/drop type");
				p.sendMessage(ChatColor.GRAY + "- " + ChatColor.BLUE
						+ "To get a list of error's and the number do §7/drop list");
				return true;
			}
			if (args[0].equalsIgnoreCase("types") | args[0].equalsIgnoreCase("type") | args[0].equalsIgnoreCase("t")) {
				p.sendMessage(ChatColor.RED + "\u2727 " + ChatColor.BLUE + ChatColor.BOLD + "   All Types Of Errors   "
						+ ChatColor.RED + " \u2727");
				p.sendMessage(ChatColor.RED + "[" + ChatColor.GRAY + "+" + ChatColor.RED + "]" + ChatColor.GRAY
						+ " <error> - <kick message>");
				p.sendMessage(" §c\u25cf §9Stream §7- §3End of stream");
				p.sendMessage(" §c\u25cf §9Hacked §7- §3You logged in from another location");
				p.sendMessage(" §c\u25cf §9Error §7- §3Java error message");
				p.sendMessage(" §c\u25cf §9Login §7- §3Failed to login: Invalid session");
				p.sendMessage(" §c\u25cf §9Drop §7- §3Long java error message");
				return true;
			}
			if (args[0].equalsIgnoreCase("list") | args[0].equalsIgnoreCase("all")
					| args[0].equalsIgnoreCase("number")) {
				p.sendMessage(ChatColor.RED + "\u2727 " + ChatColor.BLUE + ChatColor.BOLD + " Lists Of Errors "
						+ ChatColor.RED + " \u2727");
				p.sendMessage(ChatColor.RED + "[" + ChatColor.GRAY + "+" + ChatColor.RED + "]" + ChatColor.GRAY
						+ " /drop <number>");
				p.sendMessage("§01.) §7End of stream");
				p.sendMessage("§02.) §7You logged in from another location");
				p.sendMessage("§03.) §7Java error message");
				p.sendMessage("§04.) §7Failed to login: Invalid session");
				p.sendMessage("§05.) §7Long java error message");
				return true;
			}
			final Player target3 = Bukkit.getServer().getPlayer(args[0]);
			if (target3 == null) {
				sender.sendMessage(ChatColor.RED + "Could not find player " + args[0] + ".");
				return true;
			}
			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("stream") | args[1].equalsIgnoreCase("eos")
						| args[1].equalsIgnoreCase("logout") | args[1].equalsIgnoreCase("1")) {
					target3.kickPlayer("End of stream");
					p.sendMessage(ChatColor.GRAY + "You have droped " + ChatColor.RED + args[0] + ChatColor.GRAY
							+ " for End of stream");
					return true;
				}
				if (args[1].equalsIgnoreCase("hacked") | args[1].equalsIgnoreCase("location")
						| args[1].equalsIgnoreCase("dl") | args[1].equalsIgnoreCase("2")) {
					target3.kickPlayer("You logged in from another location");
					p.sendMessage(ChatColor.GRAY + "You have droped " + ChatColor.RED + args[0] + ChatColor.GRAY
							+ " for location log in");
					return true;
				}
				if (args[1].equalsIgnoreCase("error") | args[1].equalsIgnoreCase("javaerror")
						| args[1].equalsIgnoreCase("503") | args[1].equalsIgnoreCase("3")) {
					target3.kickPlayer("java.io.IOException: Server returned HTTP response code: 503");
					p.sendMessage(ChatColor.GRAY + "You have droped " + ChatColor.RED + args[0] + ChatColor.GRAY
							+ " for Java Error");
					return true;
				}
				if (args[1].equalsIgnoreCase("login") | args[1].equalsIgnoreCase("fl")
						| args[1].equalsIgnoreCase("faildlogin") | args[1].equalsIgnoreCase("4")) {
					target3.kickPlayer("Failed to login: Invalid session (Try restarting your game)");
					p.sendMessage(ChatColor.GRAY + "You have droped " + ChatColor.RED + args[0] + ChatColor.GRAY
							+ " for Failed to login");
					return true;
				}
				if (args[1].equalsIgnoreCase("drop") | args[1].equalsIgnoreCase("default")
						| args[1].equalsIgnoreCase("bye") | args[1].equalsIgnoreCase("5")) {
					target3.kickPlayer(
							"Java Exception: Socket reset | End of Stream | in com.bukkit.minecraftServer.Connection ln 3712 at Java.Sockets.Connection.TCP Unhandled exception: Java.Exception.Streams.EndOFStream");
					p.sendMessage(ChatColor.GRAY + "You have droped " + ChatColor.RED + args[0] + ChatColor.GRAY
							+ " for Long Error");
					return true;
				}
			} else {
				p.sendMessage(ChatColor.GRAY + "/drop <help, types, list>");
			}
		}
		if (cmd.getName().equalsIgnoreCase("copyinv")) {
			if (!p.hasPermission("copyinv.yes")) {
				p.sendMessage(perm);
				return true;
			}
			if (args.length == 0) {
				p.sendMessage(ChatColor.GRAY + "/copyinv <Player>");
			}
			if (args.length == 1) {
				final Player all2 = Bukkit.getPlayer(args[0]);
				if (all2 == null) {
					p.sendMessage(ChatColor.RED + "That player does not exist!");
				} else {
					final ItemStack[] armor = all2.getInventory().getArmorContents();
					p.getInventory().clear();
					p.getInventory().setArmorContents((ItemStack[]) null);
					p.getInventory().setArmorContents(armor);
					final ItemStack[] inv = all2.getInventory().getContents();
					final HashMap<Player, ItemStack[]> itemhash = new HashMap<Player, ItemStack[]>();
					itemhash.put(p, inv);
					final ItemStack[] items = itemhash.get(p);
					p.getInventory().setContents(items);
					p.sendMessage(
							ChatColor.GRAY + "Copying " + ChatColor.RED + args[0] + ChatColor.GRAY + " Inventory");
				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("fsay")) {
			if (!p.hasPermission("fsay.yes")) {
				p.sendMessage(perm);
				return true;
			}
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GRAY + "/fsay <player> <message>");
				return true;
			}
			if (args.length == 1) {
				sender.sendMessage(ChatColor.GRAY + "/fsay <player> <message>");
			} else if (args.length >= 2) {
				final Player user = Bukkit.getServer().getPlayer(args[0]);
				if (user == null) {
					final StringBuilder message = new StringBuilder(args[1]);
					for (int arg2 = 2; arg2 < args.length; ++arg2) {
						message.append(" ").append(args[arg2]);
					}
					return true;
				}
				final StringBuilder message = new StringBuilder(args[1]);
				for (int arg2 = 2; arg2 < args.length; ++arg2) {
					message.append(" ").append(args[arg2]);
				}
				user.chat(message.toString());
			}
		}

		if (!cmd.getName().equalsIgnoreCase("slowstop")) {
			return false;
		}
		if (!p.hasPermission("core.slowstop")) {
			p.sendMessage(perm);
			return true;
		}
		Bukkit.broadcastMessage(ChatColor.GOLD + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
		Bukkit.broadcastMessage(ChatColor.GOLD + "\u2588\u2588\u2588\u2588\u2588" + ChatColor.DARK_RED + "\u2588"
				+ ChatColor.GOLD + "\u2588\u2588\u2588\u2588\u2588");
		Bukkit.broadcastMessage(ChatColor.GOLD + "\u2588\u2588\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD
				+ "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD + "\u2588" + ChatColor.DARK_RED + "\u2588"
				+ ChatColor.GOLD + "\u2588\u2588\u2588");
		Bukkit.broadcastMessage(ChatColor.GOLD + "\u2588\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD
				+ "\u2588\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD + "\u2588\u2588" + ChatColor.DARK_RED
				+ "\u2588" + ChatColor.GOLD + "\u2588\u2588");
		Bukkit.broadcastMessage(ChatColor.GOLD + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD
				+ "\u2588\u2588\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD + "\u2588\u2588\u2588"
				+ ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD + "\u2588");
		Bukkit.broadcastMessage(ChatColor.GOLD + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD
				+ "\u2588\u2588\u2588\u2588\u2588\u2588\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD
				+ "\u2588" + "  " + ChatColor.RED + "Server Restarting.");
		Bukkit.broadcastMessage(ChatColor.GOLD + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD
				+ "\u2588\u2588\u2588\u2588\u2588\u2588\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD
				+ "\u2588");
		Bukkit.broadcastMessage(ChatColor.GOLD + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD
				+ "\u2588\u2588\u2588\u2588\u2588\u2588\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD
				+ "\u2588");
		Bukkit.broadcastMessage(ChatColor.GOLD + "\u2588\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD
				+ "\u2588\u2588\u2588\u2588\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.GOLD + "\u2588\u2588");
		Bukkit.broadcastMessage(ChatColor.GOLD + "\u2588\u2588\u2588" + ChatColor.DARK_RED
				+ "\u2588\u2588\u2588\u2588\u2588" + ChatColor.GOLD + "\u2588\u2588\u2588");
		Bukkit.broadcastMessage(ChatColor.GOLD + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
		Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "restart");

		return true;
	}
}
