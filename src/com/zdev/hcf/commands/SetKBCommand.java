package com.zdev.hcf.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.BaseCommand;

public class SetKBCommand extends BaseCommand {
	public SetKBCommand() {
		super("setkb", "Sets the kb.");
		setAliases(new String[] { "setknockback", "kb" });
		setUsage("/(command)");
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "knockback 0.9 0.86 1");
		Command.broadcastCommandMessage(sender, ChatColor.GREEN + "Knockback has been set!");
		return true;
	}

}