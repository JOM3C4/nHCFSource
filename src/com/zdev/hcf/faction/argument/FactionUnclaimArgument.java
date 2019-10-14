package com.zdev.hcf.faction.argument;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.faction.FactionMember;
import com.zdev.hcf.faction.claim.Claim;
import com.zdev.hcf.faction.struct.Role;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.util.chat.ClickAction;
import com.zdev.hcf.util.chat.Text;
import com.zdev.hcf.util.command.CommandArgument;

public class FactionUnclaimArgument extends CommandArgument {

	private final Base plugin;

	public FactionUnclaimArgument(final Base plugin) {
		super("unclaim", "Unclaims land from your faction.");
		this.plugin = plugin;
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName() + " ";
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can un-claim land from a faction.");
			return true;
		}
		final Player player = (Player) sender;
		final PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
		if (playerFaction == null) {
			sender.sendMessage(ChatColor.RED + "You are not in a faction.");
			return true;
		}
		final FactionMember factionMember = playerFaction.getMember(player);
		if (factionMember.getRole() != Role.LEADER) {
			sender.sendMessage(ChatColor.RED + "You must be a faction leader to unclaim land.");
			return true;
		}
		final Collection<Claim> factionClaims = playerFaction.getClaims();
		if (factionClaims.isEmpty()) {
			sender.sendMessage(ChatColor.RED + "Your faction does not own any claims.");
			return true;
		}

		if (args.length == 2) {
			if (args[1].equalsIgnoreCase("yes")) {
				for (Claim claims : factionClaims) {
					playerFaction.removeClaim(claims, player);
				}
				factionClaims.clear();
				return true;
			}
			if (args[1].equalsIgnoreCase("no")) {
				player.sendMessage(ChatColor.YELLOW + "You have been removed the unclaim-set.");
				return true;
			}
		}
		new Text(ChatColor.YELLOW + "Do you want to unclaim " + ChatColor.BOLD + "all" + ChatColor.YELLOW
				+ " of your land?").send(player);
		new Text(ChatColor.YELLOW + "If so, " + ChatColor.DARK_GREEN + "/f unclaim yes" + ChatColor.YELLOW
				+ " otherwise do" + ChatColor.DARK_RED + " /f unclaim no" + ChatColor.GRAY + " (Click here to unclaim)")
						.setColor(ChatColor.GRAY).setHoverText(ChatColor.GRAY + "Click here to unclaim all")
						.setClick(ClickAction.RUN_COMMAND, "/f unclaim yes").send(player);
		return true;
	}

}