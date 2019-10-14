package com.zdev.hcf.commands;

import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.timer.PlayerTimer;

public class GoppleCommand implements CommandExecutor, TabCompleter {
	private final Base plugin;

	public GoppleCommand(Base plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
			return true;
		}
		Player player = (Player) sender;
		PlayerTimer timer = this.plugin.getTimerManager().gappleTimer;
		long remaining = timer.getRemaining(player);
		if (remaining <= 0L) {
			sender.sendMessage(ChatColor.RED + "No active Gopple timer.");
			return true;
		}
		sender.sendMessage(ChatColor.GRAY + "Your " + ChatColor.AQUA + timer.getDisplayName() + ChatColor.GRAY
				+ " timer is active for another " + ChatColor.AQUA + Base.getRemaining(remaining, true, false)
				+ ChatColor.GRAY + '.');
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return Collections.emptyList();
	}
}
