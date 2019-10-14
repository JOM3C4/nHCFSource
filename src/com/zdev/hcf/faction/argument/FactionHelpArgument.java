package com.zdev.hcf.faction.argument;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.zdev.hcf.faction.FactionExecutor;
import com.zdev.hcf.util.BukkitUtils;
import com.zdev.hcf.util.JavaUtils;
import com.zdev.hcf.util.chat.ClickAction;
import com.zdev.hcf.util.chat.Text;
import com.zdev.hcf.util.command.CommandArgument;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionHelpArgument extends CommandArgument {
	private static final int HELP_PER_PAGE = 10;
	private final FactionExecutor executor;
	private ImmutableMultimap<Integer, Text> pages;

	public FactionHelpArgument(final FactionExecutor executor) {
		super("help", "View help on how to use factions.");
		this.executor = executor;
		this.isPlayerOnly = true;
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName();
	}

	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (args.length < 2) {
			this.showPage(sender, label, 1);
			return true;
		}
		final Integer page = JavaUtils.tryParseInt(args[1]);
		if (page == null) {
			sender.sendMessage(ChatColor.RED + "'" + args[1] + "' is not a command.");
			return true;
		}
		this.showPage(sender, label, page);
		return true;
	}

	private void showPage(final CommandSender sender, final String label, final int pageNumber) {
		if (this.pages == null) {
			final boolean isPlayer = sender instanceof Player;
			int val = 1;
			int count = 0;
			final Multimap<Integer, Text> pages = ArrayListMultimap.create();
			for (final CommandArgument argument : this.executor.getArguments()) {
				if (argument.equals((Object) this)) {
					continue;
				}
				final String permission = argument.getPermission();
				if (permission != null && !sender.hasPermission(permission)) {
					continue;
				}
				if (argument.isPlayerOnly() && !isPlayer) {
					continue;
				}
				++count;
				pages.get(val)
						.add(new Text(ChatColor.BLUE + "   /" + label + ' ' + argument.getName() + ChatColor.BLUE
								+ " > " + ChatColor.GRAY + argument.getDescription()).setColor(ChatColor.GOLD)
										.setClick(ClickAction.SUGGEST_COMMAND, "/" + label + " " + argument.getName()));
				if (count % HELP_PER_PAGE != 0) {
					continue;
				}
				++val;
			}
			this.pages = ImmutableMultimap.copyOf(pages);
		}
		final int totalPageCount = this.pages.size() / HELP_PER_PAGE;
		if (pageNumber < 1) {
			sender.sendMessage(ChatColor.RED + "You cannot view a page less than 1.");
			return;
		}
		if (pageNumber > totalPageCount) {
			sender.sendMessage(ChatColor.RED + "There are only " + totalPageCount + " pages.");
			return;
		}
		if (pageNumber == 1) {
			sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
			sender.sendMessage(C("&9General Commands:"));
			sender.sendMessage(C("&e/f create <teamName> &7- Create a new team."));
			sender.sendMessage(C("&e/f accept <teamName> &7- Accept a pending invitation."));
			sender.sendMessage(C("&e/f leave &7- Leave your current team."));
			sender.sendMessage(C("&e/f home &7- Teleport to your team home."));
			sender.sendMessage(C("&e/f stuck &7- Teleport out of enemy territory."));
			sender.sendMessage(C("&e/f deposit <amount&7|&aall> &7- Deposit money into your team balance."));
			sender.sendMessage("");
			sender.sendMessage(C("&9Informational Commands:"));
			sender.sendMessage(C("&e/f who <player&7|&eteamName> &7- Display team information."));
			sender.sendMessage(C("&e/f map &7- Show nearby claims (identified by pillars)"));
			sender.sendMessage(C("&e/f list &7- Show list of teams online (sorted by most online)"));
			sender.sendMessage("");
			sender.sendMessage(C("&9Captain Commands:"));
			sender.sendMessage(C("&e/f invite <player> &7- Invite a player to your team."));
			sender.sendMessage(C("&e/f uninvite <player> &7- Revoke an invitation."));
			sender.sendMessage(C("&e/f invites &7- List all open invitations."));
			sender.sendMessage(C("&e/f kick <player> &7- Kick a player from your team."));
			sender.sendMessage(C("&e/f claim &7- Start a claim for your team."));
			sender.sendMessage(C("&e/f sethome &7- Set your team's home at your current location."));
			sender.sendMessage(C("&e/f withdraw <amount> &7- Withdraw money from your team's balance."));
			sender.sendMessage(C("&e/f announcement <message here> &7- Set your team's announcement."));
			sender.sendMessage("");
			sender.sendMessage(C("&9Leader Commands:"));
			sender.sendMessage(C("&e/f coleader <player> &7- Add a coleader to your team."));
			sender.sendMessage(C("&e/f captain <player> &7- Add a captain to your team."));
			sender.sendMessage(C("&e/f rename <newName> &7- Rename your tema."));
			sender.sendMessage(C("&e/f disband &7- Disband your team."));
			sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		}

	}

	public String C(String string) {
		String message = ChatColor.translateAlternateColorCodes('&', string);
		return message;
	}
}
