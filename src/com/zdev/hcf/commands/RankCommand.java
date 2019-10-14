package com.zdev.hcf.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class RankCommand implements CommandExecutor {

	@SuppressWarnings({ "deprecation" })
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("rank")) {
			Player p = (Player) s;
			if (p.isOp()) {
				if (args.length != 2) {
					p.sendMessage("§cCorrect Usage: /rank <player> <rank>");
				} else {
					if (PermissionsEx.getPermissionManager().getGroupNames().contains(args[1])) {
						if (Bukkit.getServer().getPlayer(args[0]) != null) {
							Player t = Bukkit.getServer().getPlayer(args[0]);
							for (PermissionGroup i : PermissionsEx.getPermissionManager().getUser(t).getGroups()) {
								PermissionsEx.getUser(t).removeGroup(i);
							}
							PermissionsEx.getPermissionManager().getUser(t)
									.addGroup(PermissionsEx.getPermissionManager().getGroup(args[1]));
							p.sendMessage(
									"§7Set §b§l" + t.getName() + " §7to the group §b§l" + args[1] + " §7successfully.");
							t.sendMessage("§b§l" + p.getName() + " §7set your rank to §b§l" + args[1]);
							for (Player all : Bukkit.getServer().getOnlinePlayers()) {
								if (all.isOp()) {
									if (all.getName() != p.getName()) {
										all.sendMessage("§8(§3!§8) §b§l" + p.getName() + " §7set §b§l" + args[0]
												+ "'s §r§7rank to §b§l" + args[1]);
									}
								}
							}
						} else {
							p.sendMessage("§cError: The player §l" + args[0] + " §cwas not found!");
						}
					} else {
						p.sendMessage("§8§m------------------------------------------------------------");
						p.sendMessage("§a§l Groups §7(PermissionsEx)");
						p.sendMessage("");
						int number = 0;
						for (PermissionGroup i : PermissionsEx.getPermissionManager().getGroupList()) {
							number = number + 1;
							p.sendMessage("§8(§a#" + number + "§8) §a" + i.getName());
						}
						p.sendMessage("");
						p.sendMessage("§4§l WARNING: §7The rank is CaSe SeNsItIvE!");
						p.sendMessage("§7§m------------------------------------------------------------");
					}
				}
			} else {
				p.sendMessage("§cOops! Seems like you don't have a high enough rank!");
			}
		}

		return true;
	}

}