package com.zdev.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.timer.type.StuckTimer;
import com.zdev.hcf.util.command.CommandArgument;

public class FactionStuckArgument extends CommandArgument {
	private final Base plugin;

	public FactionStuckArgument(Base plugin) {
		super("stuck", "Teleport to a safe position.", new String[] { "trap", "trapped" });
		this.plugin = plugin;
	}

	public String getUsage(String label) {
		return '/' + label + ' ' + getName();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
			return true;
		}
		StuckTimer stuckTimer = this.plugin.getTimerManager().stuckTimer;
		Player player = (Player) sender;
		if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
			sender.sendMessage(ChatColor.RED + "You can only use this command from the overworld.");
			return true;
		}
		if (!stuckTimer.setCooldown(player, player.getUniqueId())) {
			sender.sendMessage(ChatColor.RED + "Your " + stuckTimer.getDisplayName() + ChatColor.RED
					+ " timer is already active.");
			return true;
		}
		sender.sendMessage(ChatColor.RED + stuckTimer.getDisplayName() + ChatColor.RED + " timer has started. "
				+ "Teleportation will commence in " + ChatColor.LIGHT_PURPLE
				+ Base.getRemaining(stuckTimer.getRemaining(player), true, false) + ChatColor.RED + ". "
				+ "This will cancel if you move more than " + 5 + " blocks.");
		return true;
	}
}
