package com.zdev.hcf.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;

public class SpawnCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("spawn")) {
			if (args.length == 0) {
				sendLocation(player);
				return true;
			}

			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				player.sendMessage(ChatColor.RED + "Player isnt online");
				return true;
			}
			player.sendMessage(ChatColor.GREEN + target.getName() + " has been teleported to spawn!");
			sendLocation(target);
			return true;
		}
		if ((cmd.getName().equalsIgnoreCase("setspawn")) && (cmd.getName().equalsIgnoreCase("setspawn"))) {
			Base.getPlugin().getConfig().set("spawn.world", player.getLocation().getWorld().getName());
			Base.getPlugin().getConfig().set("spawn.x", Double.valueOf(player.getLocation().getX()));
			Base.getPlugin().getConfig().set("spawn.y", Double.valueOf(player.getLocation().getY()));
			Base.getPlugin().getConfig().set("spawn.z", Double.valueOf(player.getLocation().getZ()));
			Base.getPlugin().getConfig().set("spawn.yaw", Float.valueOf(player.getLocation().getYaw()));
			Base.getPlugin().getConfig().set("spawn.pitch", Float.valueOf(player.getLocation().getPitch()));
			Base.getPlugin().saveConfig();
			player.sendMessage(ChatColor.GREEN + "Spawn set!");
			return true;
		}
		return false;
	}

	public static boolean sendLocation(Player player) {
		World w = Bukkit.getServer().getWorld(Base.getPlugin().getConfig().getString("spawn.world"));
		double x = Base.getPlugin().getConfig().getDouble("spawn.x");
		double y = Base.getPlugin().getConfig().getDouble("spawn.y");
		double z = Base.getPlugin().getConfig().getDouble("spawn.z");
		float yaw = (float) Base.getPlugin().getConfig().getDouble("spawn.yaw");
		float pitch = (float) Base.getPlugin().getConfig().getDouble("spawn.pitch");
		player.teleport(new Location(w, x, y, z, yaw, pitch));
		return false;
	}
}