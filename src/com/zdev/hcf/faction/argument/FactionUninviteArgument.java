package com.zdev.hcf.faction.argument;

import com.google.common.collect.ImmutableList;
import com.zdev.hcf.Base;
import com.zdev.hcf.faction.FactionMember;
import com.zdev.hcf.faction.struct.Role;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.util.command.CommandArgument;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class FactionUninviteArgument extends CommandArgument {
	private static final ImmutableList<String> COMPLETIONS;

	static {
		COMPLETIONS = ImmutableList.of("all");
	}

	private final Base plugin;

	public FactionUninviteArgument(final Base plugin) {
		super("uninvite", "Revoke an invitation to a player.", new String[] { "deinvite", "deinv", "uninv", "revoke" });
		this.plugin = plugin;
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName() + " <all|playerName>";
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can un-invite from a faction.");
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
		final FactionMember factionMember = playerFaction.getMember(player);
		if (factionMember.getRole() == Role.MEMBER) {
			sender.sendMessage(ChatColor.RED + "You must be a captain to un-invite players.");
			return true;
		}
		final Set<String> invitedPlayerNames = playerFaction.getInvitedPlayerNames();
		if (args[1].equalsIgnoreCase("all")) {
			invitedPlayerNames.clear();
			sender.sendMessage(ChatColor.GRAY + "You have cleared all pending invitations.");
			return true;
		}
		if (!invitedPlayerNames.remove(args[1])) {
			sender.sendMessage(ChatColor.RED + "There is not a pending invitation for '" + args[1] + "'!");
			return true;
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
		if (playerFaction == null || playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
			return Collections.emptyList();
		}
		final List<String> results = new ArrayList<String>(
				(Collection<? extends String>) FactionUninviteArgument.COMPLETIONS);
		results.addAll(playerFaction.getInvitedPlayerNames());
		return results;
	}
}
