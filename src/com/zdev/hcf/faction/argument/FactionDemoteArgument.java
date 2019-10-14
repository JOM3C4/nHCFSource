package com.zdev.hcf.faction.argument;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.faction.FactionMember;
import com.zdev.hcf.faction.struct.Relation;
import com.zdev.hcf.faction.struct.Role;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.util.command.CommandArgument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FactionDemoteArgument extends CommandArgument {
	private final Base plugin;

	public FactionDemoteArgument(final Base plugin) {
		super("demote", "Demotes a player to a member.", new String[] { "uncaptain", "delcaptain", "delofficer" });
		this.plugin = plugin;
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName() + " <playerName>";
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		final Player player = (Player) sender;
		final PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
		if (playerFaction == null) {
			sender.sendMessage(ChatColor.RED + "You are not in a faction.");
			return true;
		}
		Role playerRole = playerFaction.getMember(player.getUniqueId()).getRole();

		if (playerRole == Role.MEMBER || playerRole == Role.CAPTAIN) {
			sender.sendMessage(ChatColor.RED + "You must be a co-leader or higher to edit the faction roster.");
			return true;
		}

		FactionMember targetMember = playerFaction.getMember(args[1]);

		if (targetMember == null) {
			sender.sendMessage(ChatColor.RED + "That player is not in your faction.");
			return true;
		}

		if (targetMember.getRole() == Role.LEADER || targetMember.getRole() == Role.MEMBER) {
			sender.sendMessage(ChatColor.RED + "You can only demote faction captains or co-leaders.");
			return true;
		}

		if (playerRole == Role.LEADER && targetMember.getRole() == Role.COLEADER) {
			targetMember.setRole(Role.CAPTAIN);
			playerFaction.broadcast(Relation.MEMBER.toChatColour() + targetMember.getName() + ChatColor.YELLOW
					+ " has been demoted from a faction co-leader.");
			return true;
		}

		targetMember.setRole(Role.MEMBER);
		playerFaction.broadcast(Relation.MEMBER.toChatColour() + targetMember.getName() + ChatColor.YELLOW
				+ " has been demoted from a faction captain.");
		return true;
	}

	@SuppressWarnings("deprecation")
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (args.length != 2 || !(sender instanceof Player)) {
			return Collections.emptyList();
		}
		final Player player = (Player) sender;
		final PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
		if (playerFaction == null || playerFaction.getMember(player.getUniqueId()).getRole() != Role.LEADER) {
			return Collections.emptyList();
		}
		final List<String> results = new ArrayList<String>();
		final Collection<UUID> keySet = playerFaction.getMembers().keySet();
		for (final UUID entry : keySet) {
			final OfflinePlayer target = Bukkit.getOfflinePlayer(entry);
			final String targetName = target.getName();
			if (targetName != null && playerFaction.getMember(target.getUniqueId()).getRole() == Role.CAPTAIN) {
				results.add(targetName);
			}
		}
		return results;
	}
}
