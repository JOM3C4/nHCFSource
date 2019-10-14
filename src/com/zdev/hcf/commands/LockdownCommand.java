package com.zdev.hcf.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.zdev.hcf.Base;

public class LockdownCommand implements CommandExecutor, Listener {
	private Base mainPlugin;
	private boolean lockdownEnabled;
	private List<String> lockdown;

	public LockdownCommand(Base mainPlugin) {
		this.mainPlugin = mainPlugin;
		this.lockdownEnabled = false;
		this.lockdown = new ArrayList<String>();
		this.mainPlugin.getServer().getPluginManager().registerEvents(this, this.mainPlugin);
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if ((this.lockdownEnabled) && (!this.lockdown.contains(e.getPlayer().getName().toLowerCase()))) {
			e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST,
					ChatColor.RED + "HCStatic is currently on lockdown mode");
		}
	}

	public boolean onCommand(CommandSender s, Command c, String alias, String[] args) {
		if (!s.hasPermission("core.lockdown")) {
			s.sendMessage(ChatColor.RED + "No permission.");

			return true;

		}
		if ((args.length < 1) || (args.length > 2)) {
			s.sendMessage(ChatColor.RED + "Correct Usage: /" + c.getName() + " <on, off, list, add, remove> [name]");
			return true;
		}
		if (args[0].equalsIgnoreCase("on")) {
			this.lockdownEnabled = true;
			s.sendMessage(ChatColor.GREEN + "Lockdown enabled.");

		} else if (args[0].equalsIgnoreCase("off")) {
			this.lockdownEnabled = false;
			s.sendMessage(ChatColor.RED + "Lockdown disabled.");
		} else if (args[0].equalsIgnoreCase("list")) {
			s.sendMessage("Lockdown added players: "
					+ StringUtils.join(this.lockdown.toArray(), ", ", 0, this.lockdown.size()) + ".");
		} else if (args[0].equalsIgnoreCase("add")) {
			if (!this.lockdown.contains(args[1].toLowerCase())) {
				this.lockdown.add(args[1].toLowerCase());
			}
			s.sendMessage(ChatColor.GREEN + "Added " + args[1] + ".");
		} else if (args[0].equalsIgnoreCase("remove")) {
			if (this.lockdown.contains(args[1].toLowerCase())) {
				this.lockdown.remove(args[1].toLowerCase());
			}
			s.sendMessage(ChatColor.GREEN + "Removed " + args[1] + ".");
		}
		return true;
	}
}