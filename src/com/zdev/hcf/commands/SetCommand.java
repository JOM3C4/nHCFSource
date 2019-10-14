package com.zdev.hcf.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.zdev.hcf.Base;
import com.zdev.hcf.config.WorldData;
import com.zdev.hcf.util.BukkitUtils;

public class SetCommand implements CommandExecutor, Listener {
	public SetCommand(Base plugin) {
	}

	public boolean onCommand(CommandSender s, Command c, String alias, String[] args) {
		if (!s.hasPermission("core.admin")) {
			s.sendMessage(ChatColor.RED + "You do not have permission to perform this command.");
			return true;
		}
		Player p = (Player) s;
		if ((args.length != 1)) {
			p.sendMessage(ChatColor.RED + "/set <exit|spawn>");
			p.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
			p.sendMessage(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "End Exit and Spawn Location");
			p.sendMessage(ChatColor.GRAY + " /set spawn: " + ChatColor.RESET + "Set the location for end-spawn.");
			p.sendMessage(ChatColor.GRAY + " /set exit: " + ChatColor.RESET + "Set the location for end-exit.");
			p.sendMessage(ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
			return true;

		} else if (args[0].equalsIgnoreCase("exit")) {
			Location loc = p.getLocation();
			WorldData.getInstance().getConfig().set("world.exit.x", Double.valueOf(loc.getX()));
			WorldData.getInstance().getConfig().set("world.exit.y", Double.valueOf(loc.getY()));
			WorldData.getInstance().getConfig().set("world.exit.z", Double.valueOf(loc.getZ()));
			WorldData.getInstance().saveConfig();
			p.sendMessage(ChatColor.GREEN + "You have set the location for end exit.");
		} else if (args[0].equalsIgnoreCase("spawn")) {
			Location loc = p.getLocation();
			WorldData.getInstance().getConfig().set("world.end.entrace.x", Double.valueOf(loc.getX()));
			WorldData.getInstance().getConfig().set("world.end.entrace.y", Double.valueOf(loc.getY()));
			WorldData.getInstance().getConfig().set("world.end.entrace.z", Double.valueOf(loc.getZ()));
			WorldData.getInstance().saveConfig();
			p.sendMessage(ChatColor.GREEN + "You have set the location for end spawn.");

		}
		return true;
	}

}
