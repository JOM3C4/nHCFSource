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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FactionPromoteArgument extends CommandArgument {
	private final Base plugin;

	public FactionPromoteArgument(final Base plugin) {
		super("promote", "Promotes a player to a captain.");
		this.plugin = plugin;
		this.aliases = new String[] { "captain", "officer", "mod", "moderator" };
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName() + " <playerName>";
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can set faction captains.");
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		final Player player = (Player) sender;
		final UUID uuid = player.getUniqueId();
		final PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(uuid);
		if (playerFaction == null) {
			sender.sendMessage(ChatColor.RED + "You are not in a faction.");
			return true;
		}
		if (playerFaction.getMember(uuid).getRole() != Role.LEADER) {
			sender.sendMessage(ChatColor.RED + "You must be a faction leader to assign members as a captain.");
			return true;
		}

		FactionMember targetMember = playerFaction.getMember(args[1]);

		if (targetMember == null) {
			sender.sendMessage(ChatColor.RED + "That player is not in your faction.");
			return true;
		}

		if (targetMember.getRole() == Role.COLEADER) {
			sender.sendMessage(ChatColor.RED + "You can only assign co-leader to captains, " + targetMember.getName()
					+ " is a " + targetMember.getRole().getName() + '.');
			return true;
		}

		if (targetMember.getRole() == Role.MEMBER) {
			Role role = Role.CAPTAIN;
			targetMember.setRole(role);

			playerFaction.broadcast(Relation.MEMBER.toChatColour() + role.getAstrix() + targetMember.getName()
					+ ChatColor.YELLOW + " has been assigned as a faction captain.");
		} else if (targetMember.getRole() == Role.CAPTAIN) {
			Role role = Role.COLEADER;
			targetMember.setRole(role);

			playerFaction.broadcast(Relation.MEMBER.toChatColour() + role.getAstrix() + targetMember.getName()
					+ ChatColor.YELLOW + " has been assigned as a faction co-leader.");
		}
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
		for (final Map.Entry<UUID, FactionMember> entry : playerFaction.getMembers().entrySet()) {
			if (entry.getValue().getRole() == Role.MEMBER) {
				final OfflinePlayer target = Bukkit.getOfflinePlayer((UUID) entry.getKey());
				final String targetName = target.getName();
				if (targetName == null) {
					continue;
				}
				results.add(targetName);
			}
		}
		return results;
	}
}
