package com.zdev.hcf.faction.argument.staff;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.Base;
import com.zdev.hcf.faction.FactionManager;
import com.zdev.hcf.faction.type.Faction;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.util.JavaUtils;
import com.zdev.hcf.util.command.CommandArgument;

public class FactionSetDtrRegenArgument extends CommandArgument {
	private final Base plugin;

	public FactionSetDtrRegenArgument(final Base plugin) {
		super("setdtrregen", "Sets the DTR cooldown of a faction.", new String[] { "setdtrregeneration" });
		this.plugin = plugin;
		this.permission = "command.faction.argument." + this.getName();
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName() + " <playerName|factionName> <newRegen>";
	}

	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (args.length < 3) {
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		final long newRegen = JavaUtils.parse(args[2]);
		if (newRegen == -1L) {
			sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
			return true;
		}
		if (newRegen > FactionManager.MAX_DTR_REGEN_MILLIS) {
			sender.sendMessage(
					ChatColor.RED + "Cannot set factions DTR regen above " + FactionManager.MAX_DTR_REGEN_WORDS + ".");
			return true;
		}
		final Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
		if (faction == null) {
			sender.sendMessage(
					ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
			return true;
		}
		if (!(faction instanceof PlayerFaction)) {
			sender.sendMessage(ChatColor.RED + "This type of faction does not use DTR.");
			return true;
		}
		final PlayerFaction playerFaction = (PlayerFaction) faction;
		final long previousRegenRemaining = playerFaction.getRemainingRegenerationTime();
		playerFaction.setRemainingRegenerationTime(newRegen);
		Command.broadcastCommandMessage(sender,
				ChatColor.YELLOW + "Set DTR regen of " + faction.getName() + " from "
						+ DurationFormatUtils.formatDurationWords(previousRegenRemaining, true, true) + " to "
						+ DurationFormatUtils.formatDurationWords(newRegen, true, true) + '.');
		return true;
	}

}
