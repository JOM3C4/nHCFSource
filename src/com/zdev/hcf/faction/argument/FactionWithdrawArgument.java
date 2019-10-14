package com.zdev.hcf.faction.argument;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import com.zdev.hcf.Base;
import com.zdev.hcf.ConfigurationService;
import com.zdev.hcf.faction.FactionMember;
import com.zdev.hcf.faction.struct.Role;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.util.JavaUtils;
import com.zdev.hcf.util.command.CommandArgument;

public class FactionWithdrawArgument extends CommandArgument {
	private static final ImmutableList<String> COMPLETIONS;

	static {
		COMPLETIONS = ImmutableList.of("all");
	}

	private final Base plugin;

	public FactionWithdrawArgument(final Base plugin) {
		super("withdraw", "Withdraws money from the faction balance.", new String[] { "w" });
		this.plugin = plugin;
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName() + " <all|amount>";
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can update the faction balance.");
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
		final UUID uuid = player.getUniqueId();
		final FactionMember factionMember = playerFaction.getMember(uuid);
		if (factionMember.getRole() == Role.MEMBER) {
			sender.sendMessage(ChatColor.RED + "You must be a captain to withdraw money.");
			return true;
		}
		final int factionBalance = playerFaction.getBalance();
		Integer amount;
		if (args[1].equalsIgnoreCase("all")) {
			amount = factionBalance;
		} else if ((amount = JavaUtils.tryParseInt(args[1])) == null) {
			sender.sendMessage(ChatColor.RED + args[1] + " is not a valid number.");
			return true;
		}
		if (amount <= 0) {
			sender.sendMessage(ChatColor.RED + "You can't deposit $0.0 (or less)!");
			return true;
		}
		if (amount > factionBalance) {
			sender.sendMessage(ChatColor.RED + "You team doesn't have enough money to do this!");
			return true;
		}
		this.plugin.getEconomyManager().addBalance(uuid, amount);
		playerFaction.setBalance(factionBalance - amount);
		playerFaction.broadcast(ChatColor.LIGHT_PURPLE + factionMember.getRole().getAstrix() + sender.getName()
				+ ChatColor.YELLOW + " withdrew " + ChatColor.LIGHT_PURPLE + '$' + JavaUtils.format((Number) amount)
				+ ChatColor.YELLOW + " from the team balance.");
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		return (List<String>) ((args.length == 2) ? FactionWithdrawArgument.COMPLETIONS : Collections.emptyList());
	}
}
