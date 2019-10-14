package com.zdev.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.faction.FactionMember;
import com.zdev.hcf.faction.struct.Role;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.util.command.CommandArgument;

public class FactionOpenArgument extends CommandArgument {
	private final Base plugin;

	public FactionOpenArgument(final Base plugin) {
		super("open", "Opens the faction to the public.");
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
		final FactionMember factionMember = playerFaction.getMember(player.getUniqueId());
		if (factionMember.getRole() != Role.LEADER) {
			sender.sendMessage(ChatColor.RED + "You must be a faction leader to do this.");
			return true;
		}
		final boolean newOpen = !playerFaction.isOpen();
		playerFaction.setOpen(newOpen);
		playerFaction.broadcast(ChatColor.DARK_GREEN + sender.getName() + ChatColor.YELLOW + " has "
				+ (newOpen ? (ChatColor.GREEN + "opened") : (ChatColor.RED + "closed")) + ChatColor.YELLOW
				+ " the faction to public.");
		return true;
	}
}
