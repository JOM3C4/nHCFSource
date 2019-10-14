package com.zdev.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.faction.struct.Role;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.util.command.CommandArgument;

public class FactionDisbandArgument extends CommandArgument {
	private final Base plugin;

	public FactionDisbandArgument(final Base plugin) {
		super("disband", "Disband your faction.");
		this.plugin = plugin;
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName();
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
			return true;
		}
		final Player player = (Player) sender;
		final PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
		if (playerFaction == null) {
			sender.sendMessage(ChatColor.RED + "You are not in a faction.");
			return true;
		}
		if (playerFaction.isRaidable() && !this.plugin.getEotwHandler().isEndOfTheWorld()) {
			sender.sendMessage(ChatColor.RED + "You cannot disband your faction while it is raidable.");
			return true;
		}
		if (playerFaction.getMember(player.getUniqueId()).getRole() != Role.LEADER) {
			sender.sendMessage(ChatColor.RED + "You must be a leader to disband the faction.");
			return true;
		}
		playerFaction.broadcast(
				ChatColor.RED.toString() + ChatColor.BOLD + sender.getName() + " has disbanded the faction.");
		this.plugin.getFactionManager().removeFaction(playerFaction, sender);
		return true;
	}
}
