package com.zdev.hcf.event.koth.argument;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.Base;
import com.zdev.hcf.event.CaptureZone;
import com.zdev.hcf.event.faction.KothFaction;
import com.zdev.hcf.faction.type.Faction;
import com.zdev.hcf.util.JavaUtils;
import com.zdev.hcf.util.command.CommandArgument;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KothSetCapDelayArgument extends CommandArgument {
	private final Base plugin;

	public KothSetCapDelayArgument(final Base plugin) {
		super("setcapdelay", "Sets the cap delay of a KOTH");
		this.plugin = plugin;
		this.aliases = new String[] { "setcapturedelay" };
		this.permission = "hcf.command.koth.argument." + this.getName();
	}

	public String getUsage(final String label) {
		return '/' + label + ' ' + this.getName() + " <kothName> <capDelay>";
	}

	public boolean onCommand(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (args.length < 3) {
			sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
			return true;
		}
		final Faction faction = this.plugin.getFactionManager().getFaction(args[1]);
		if (faction == null || !(faction instanceof KothFaction)) {
			sender.sendMessage(ChatColor.RED + "There is not a KOTH arena named '" + args[1] + "'.");
			return true;
		}
		final long duration = JavaUtils.parse(StringUtils.join((Object[]) args, ' ', 2, args.length));
		if (duration == -1L) {
			sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
			return true;
		}
		final KothFaction kothFaction = (KothFaction) faction;
		final CaptureZone captureZone = kothFaction.getCaptureZone();
		if (captureZone == null) {
			sender.sendMessage(ChatColor.RED + kothFaction.getDisplayName(sender) + ChatColor.RED
					+ " does not have a capture zone.");
			return true;
		}
		if (captureZone.isActive() && duration < captureZone.getRemainingCaptureMillis()) {
			captureZone.setRemainingCaptureMillis(duration);
		}
		captureZone.setDefaultCaptureMillis(duration);
		sender.sendMessage(ChatColor.YELLOW + "Set the capture delay of KOTH arena " + ChatColor.WHITE
				+ kothFaction.getDisplayName(sender) + ChatColor.YELLOW + " to " + ChatColor.WHITE
				+ DurationFormatUtils.formatDurationWords(duration, true, true) + ChatColor.WHITE + '.');
		return true;
	}

	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label,
			final String[] args) {
		if (args.length != 2) {
			return Collections.emptyList();
		}
		return this.plugin.getFactionManager().getFactions().stream().filter(faction -> faction instanceof KothFaction)
				.map(Faction::getName).collect(Collectors.toList());
	}
}
