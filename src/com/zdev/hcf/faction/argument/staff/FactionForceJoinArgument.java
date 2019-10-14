package com.zdev.hcf.faction.argument.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.faction.FactionMember;
import com.zdev.hcf.faction.struct.ChatChannel;
import com.zdev.hcf.faction.struct.Role;
import com.zdev.hcf.faction.type.Faction;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.util.command.CommandArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FactionForceJoinArgument extends CommandArgument {
	private final Base plugin;

	public FactionForceJoinArgument(final Base plugin) {
		super("forcejoin", "Forcefully join a faction.");
		this.plugin = plugin;
		this.permission = "command.faction.argument." + this.getName();
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName() + " <factionName>";
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can join factions.");
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		final Player player = (Player) sender;
		PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
		if (playerFaction != null) {
			sender.sendMessage(ChatColor.RED + "You are already in a faction.");
			return true;
		}
		final Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
		if (faction == null) {
			sender.sendMessage(
					ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
			return true;
		}
		if (!(faction instanceof PlayerFaction)) {
			sender.sendMessage(ChatColor.RED + "You can only join player factions.");
			return true;
		}
		playerFaction = (PlayerFaction) faction;
		if (playerFaction.setMember(player, new FactionMember(player, ChatChannel.PUBLIC, Role.MEMBER), true)) {
			playerFaction
					.broadcast(ChatColor.YELLOW.toString() + sender.getName() + " has joined your faction forcefully.");
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (args.length != 2 || !(sender instanceof Player)) {
			return Collections.emptyList();
		}
		if (args[1].isEmpty()) {
			return null;
		}
		final Player player = (Player) sender;
		final List<String> results = new ArrayList<String>(
				this.plugin.getFactionManager().getFactionNameMap().keySet());
		for (final Player target : Bukkit.getOnlinePlayers()) {
			if (player.canSee(target) && !results.contains(target.getName())) {
				results.add(target.getName());
			}
		}
		return results;
	}
}
