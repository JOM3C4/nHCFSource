package com.zdev.hcf.faction.argument;

import net.minecraft.server.v1_7_R4.IChatBaseComponent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.faction.struct.Relation;
import com.zdev.hcf.faction.struct.Role;
import com.zdev.hcf.faction.type.Faction;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.util.chat.ClickAction;
import com.zdev.hcf.util.chat.Text;
import com.zdev.hcf.util.command.CommandArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class FactionInviteArgument extends CommandArgument {
	private static final Pattern USERNAME_REGEX;

	static {
		USERNAME_REGEX = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");
	}

	private final Base plugin;

	public FactionInviteArgument(final Base plugin) {
		super("invite", "Invite a player to the faction.");
		this.plugin = plugin;
		this.aliases = new String[] { "inv", "invitemember", "inviteplayer" };
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName() + " <playerName>";
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can invite to a faction.");
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		if (!FactionInviteArgument.USERNAME_REGEX.matcher(args[1]).matches()) {
			sender.sendMessage(ChatColor.RED + "'" + args[1] + "' is an invalid username.");
			return true;
		}
		final Player player = (Player) sender;
		final PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
		if (playerFaction == null) {
			sender.sendMessage(ChatColor.RED + "You are not in a faction.");
			return true;
		}
		if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
			sender.sendMessage(ChatColor.RED + "You must a captain to invite members.");
			return true;
		}
		final Set<String> invitedPlayerNames = playerFaction.getInvitedPlayerNames();
		String name = args[1];
		if (playerFaction.getMember(name) != null) {
			sender.sendMessage(ChatColor.RED + "'" + name + "' is already in your faction.");
			return true;
		}
		if (!this.plugin.getEotwHandler().isEndOfTheWorld() && playerFaction.isRaidable()) {
			sender.sendMessage(ChatColor.RED + "You may not invite players whilst your faction is raidable.");
			return true;
		}
		if (!invitedPlayerNames.add(name)) {
			sender.sendMessage(ChatColor.RED + name + " has already been invited.");
			return true;
		}
		final Player target = Bukkit.getPlayer(name);
		if (target != null) {
			name = target.getName();
			final Text text = new Text(sender.getName()).setColor(ChatColor.DARK_AQUA)
					.append(new Text(" invited you to join '").setColor(ChatColor.DARK_AQUA));
			text.append(new Text(playerFaction.getName()).setColor(ChatColor.DARK_AQUA))
					.append(new Text("'. ").setColor(ChatColor.DARK_AQUA));
			text.append(new Text(ChatColor.YELLOW + "Click here").setColor(ChatColor.GREEN)
					.setClick(ClickAction.RUN_COMMAND, '/' + label + " accept " + playerFaction.getName())
					.setHoverText(ChatColor.YELLOW + "Click to join "
							+ playerFaction.getDisplayName((CommandSender) target) + ChatColor.YELLOW + '.'))
					.append((IChatBaseComponent) new Text(ChatColor.DARK_AQUA + " to accept this invitation.")
							.setColor(ChatColor.DARK_AQUA));
			text.send(target);
		}
		playerFaction.broadcast(ChatColor.YELLOW + name + " has been invited to the team.");
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
		final List<String> results = new ArrayList<String>();
		for (final Player target : Bukkit.getOnlinePlayers()) {
			if (player.canSee(target) && !results.contains(target.getName())) {
				final Faction targetFaction = this.plugin.getFactionManager().getPlayerFaction(target.getUniqueId());
				if (targetFaction != null && targetFaction.equals(playerFaction)) {
					continue;
				}
				results.add(target.getName());
			}
		}
		return results;
	}
}
