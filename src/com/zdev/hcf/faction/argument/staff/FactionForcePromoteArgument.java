package com.zdev.hcf.faction.argument.staff;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.Base;
import com.zdev.hcf.faction.FactionMember;
import com.zdev.hcf.faction.struct.Role;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.util.command.CommandArgument;

import java.util.Collections;
import java.util.List;

public class FactionForcePromoteArgument extends CommandArgument {
	private final Base plugin;

	public FactionForcePromoteArgument(final Base plugin) {
		super("forcepromote", "Forces the promotion status of a player.");
		this.plugin = plugin;
		this.permission = "command.faction.argument." + this.getName();
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName() + " <playerName>";
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		final PlayerFaction playerFaction = this.plugin.getFactionManager().getContainingPlayerFaction(args[1]);
		if (playerFaction == null) {
			sender.sendMessage(ChatColor.RED + "Faction containing member with IGN or UUID " + args[1] + " not found.");
			return true;
		}
		final FactionMember factionMember = playerFaction.getMember(args[1]);
		if (factionMember == null) {
			sender.sendMessage(ChatColor.RED + "Faction containing member with IGN or UUID " + args[1] + " not found.");
			return true;
		}
		if (factionMember.getRole() != Role.MEMBER) {
			sender.sendMessage(ChatColor.RED + factionMember.getName() + " is already a "
					+ factionMember.getRole().getName() + '.');
			return true;
		}
		factionMember.setRole(Role.CAPTAIN);
		playerFaction.broadcast(ChatColor.GOLD.toString() + ChatColor.BOLD + sender.getName()
				+ " has been forcefully assigned as a captain.");
		return true;
	}

	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		return (args.length == 2) ? null : Collections.emptyList();
	}
}
