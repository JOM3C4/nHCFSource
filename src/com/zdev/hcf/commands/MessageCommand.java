package com.zdev.hcf.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class MessageCommand implements CommandExecutor {

	public static List<String> toggle = new ArrayList<String>();
	public static HashMap<CommandSender, Player> reply = new HashMap<CommandSender, Player>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String senderprefix = PermissionsEx.getUser((Player) sender).getPrefix().replace("_", " ");
		String otherprefix = PermissionsEx.getUser((getPlayer(args[0]).getName())).getPrefix().replace("_", " ");
		if (sender instanceof Player) {
			if (args.length <= 1) {
				sender.sendMessage(ChatColor.RED + "Usage: /message <player> <message>");
			} else if (Bukkit.getPlayer(args[0]) == null) {
				sender.sendMessage(ChatColor.RED + "Player isn't online.");
			} else if (Bukkit.getPlayer(args[0]).getName() == sender.getName()) {
				sender.sendMessage(ChatColor.RED + "You cannot message yourself");
			} else if ((toggle.contains(Bukkit.getPlayer(args[0]).getDisplayName()))
					&& (!sender.hasPermission("command.message.bypass"))) {
				sender.sendMessage(MessageEvent
						.format(otherprefix + Bukkit.getPlayer(args[0]).getName() + "&7 has their messages disabled."));
			} else {
				String msgto = MessageEvent.format("&7(&fme &7to " + otherprefix + Bukkit.getPlayer(args[0]).getName()
						+ "&7)&7 " + MessageEvent.toString(args, 1));
				sender.sendMessage(msgto);

				String msgrecieve = MessageEvent.format(
						"&7(" + senderprefix + sender.getName() + "&7 to &fme&7) &7" + MessageEvent.toString(args, 1));
				Bukkit.getPlayer(args[0]).sendMessage(msgrecieve);
				reply.put(sender, Bukkit.getPlayer(args[0]));
				reply.put(Bukkit.getPlayer(args[0]), (Player) sender);
				return true;
			}
		}
		return false;
	}

	private Bukkit getPlayer(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
