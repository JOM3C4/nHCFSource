package com.zdev.hcf.faction.argument;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.util.command.CommandArgument;

import net.md_5.bungee.api.ChatColor;

public class FactionBuildArgument extends CommandArgument {

	private final Base plugin;

	public FactionBuildArgument(Base plugin) {
		super("build", "Opens the build settings GUI.", "hcf.command.faction.build", new String[] { "" });
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 1) {
				sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
				return true;
			}

		}
		return true;
	}

	@Override
	public String getUsage(String label) {
		return '/' + label + ' ' + getName();
	}

}
