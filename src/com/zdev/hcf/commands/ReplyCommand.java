package com.zdev.hcf.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ReplyCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		String senderprefix = PermissionsEx.getUser((Player) sender).getPrefix().replace("_", " ");
		String otherprefix = PermissionsEx.getUser((Player) MessageCommand.reply.get(sender)).getPrefix().replace("_",
				" ");
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Usage: /reply <message>");
		} else if (MessageCommand.reply.get(sender) == null) {
			sender.sendMessage(ChatColor.RED + "You have nobody to reply to.");
		} else if (((Player) MessageCommand.reply.get(sender)).getName() == sender.getName()) {
			sender.sendMessage(ChatColor.RED + "You cannot message yourself.");
		} else if (args[0].equalsIgnoreCase("who")) {
			sender.sendMessage(MessageEvent.format("&7You are currently in a conversation with " + otherprefix
					+ ((Player) MessageCommand.reply.get(sender)).getName()));
		} else if ((MessageCommand.toggle.contains(((Player) MessageCommand.reply.get(sender)).getName()))
				&& (!sender.hasPermission("command.message.bypass"))) {
			sender.sendMessage(MessageEvent.format(otherprefix + ((Player) MessageCommand.reply.get(sender)).getName()
					+ " has their messages disabled."));
		} else {
			String msgto = MessageEvent.format("&7(&fme &7to " + otherprefix
					+ MessageCommand.reply.get(sender).getName() + "&7)&7 " + MessageEvent.toString(args, 0));
			sender.sendMessage(msgto);
			String msgget = MessageEvent.format(
					"&7(" + senderprefix + sender.getName() + "&7 to &fme&7)&7 " + MessageEvent.toString(args, 0));
			((Player) MessageCommand.reply.get(sender)).sendMessage(MessageEvent.format(msgget));

			return true;
		}
		return false;
	}

	private Command getPlayer(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
