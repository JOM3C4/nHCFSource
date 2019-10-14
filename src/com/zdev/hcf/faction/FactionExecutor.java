package com.zdev.hcf.faction;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.Base;
import com.zdev.hcf.faction.argument.*;
import com.zdev.hcf.faction.argument.staff.*;
import com.zdev.hcf.util.command.ArgumentExecutor;
import com.zdev.hcf.util.command.CommandArgument;

public class FactionExecutor extends ArgumentExecutor {
	private final CommandArgument helpArgument;

	public FactionExecutor(final Base plugin) {
		super("faction");
		this.addArgument(new FactionLockArgument(plugin));
		this.addArgument(new FactionCoLeaderArgument(plugin));
		this.addArgument(new FactionAcceptArgument(plugin));
		this.addArgument(new FactionAllyArgument(plugin));
		this.addArgument(new FactionChatArgument(plugin));
		this.addArgument(new FactionChatSpyArgument(plugin));
		this.addArgument(new FactionClaimArgument(plugin));
		addArgument(new FactionClaimForArgument(plugin));
		this.addArgument(new FactionClaimsArgument(plugin));
		this.addArgument(new FactionClearClaimsArgument(plugin));
		this.addArgument(new FactionCreateArgument(plugin));
		this.addArgument(new FactionAnnouncementArgument(plugin));
		this.addArgument(new FactionDemoteArgument(plugin));
		this.addArgument(new FactionDisbandArgument(plugin));
		this.addArgument(new FactionSetDtrRegenArgument(plugin));
		addArgument(new FactionDepositArgument(plugin));
		addArgument(new FactionWithdrawArgument(plugin));
		this.addArgument(new FactionForceJoinArgument(plugin));
		this.addArgument(new FactionForceLeaderArgument(plugin));
		this.addArgument(new FactionMuteArgument(plugin));
		this.addArgument(new FactionForcePromoteArgument(plugin));
		this.addArgument(this.helpArgument = new FactionHelpArgument(this));
		this.addArgument(new FactionHomeArgument(this, plugin));
		this.addArgument(new FactionInviteArgument(plugin));
		this.addArgument(new FactionInvitesArgument(plugin));
		this.addArgument(new FactionKickArgument(plugin));
		this.addArgument(new FactionLeaderArgument(plugin));
		this.addArgument(new FactionLeaveArgument(plugin));
		this.addArgument(new FactionListArgument(plugin));
		this.addArgument(new FactionMapArgument(plugin));
		this.addArgument(new FactionMessageArgument(plugin));
		this.addArgument(new FactionOpenArgument(plugin));
		this.addArgument(new FactionRemoveArgument(plugin));
		this.addArgument(new FactionRenameArgument(plugin));
		this.addArgument(new FactionPromoteArgument(plugin));
		this.addArgument(new FactionSetDtrArgument(plugin));
		this.addArgument(new FactionSetDeathbanMultiplierArgument(plugin));
		this.addArgument(new FactionSetHomeArgument(plugin));
		this.addArgument(new FactionShowArgument(plugin));
		this.addArgument(new FactionStuckArgument(plugin));
		this.addArgument(new FactionUnclaimArgument(plugin));
		this.addArgument(new FactionUnallyArgument(plugin));
		this.addArgument(new FactionUninviteArgument(plugin));
		this.addArgument(new FactionBuildArgument(plugin));
		this.addArgument(new FactionManagerArgument(plugin));
	}

	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (args.length < 1) {
			this.helpArgument.onCommand(sender, command, label, args);
			return true;
		}

		CommandArgument argument = getArgument(args[0]);
		if (argument != null) {
			String permission = argument.getPermission();
			if (permission == null || sender.hasPermission(permission)) {
				argument.onCommand(sender, command, label, args);
				return true;
			}
		}

		this.helpArgument.onCommand(sender, command, label, args);
		return true;
	}

}
