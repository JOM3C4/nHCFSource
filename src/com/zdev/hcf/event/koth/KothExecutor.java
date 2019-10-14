package com.zdev.hcf.event.koth;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.Base;
import com.zdev.hcf.event.koth.argument.KothNextArgument;
import com.zdev.hcf.event.koth.argument.KothScheduleArgument;
import com.zdev.hcf.event.koth.argument.KothSetCapDelayArgument;
import com.zdev.hcf.event.koth.argument.KothShowArgument;
import com.zdev.hcf.util.command.ArgumentExecutor;

public class KothExecutor extends ArgumentExecutor {
	private final KothScheduleArgument kothScheduleArgument;

	public KothExecutor(Base plugin) {
		super("koth");
		addArgument(new KothNextArgument(plugin));
		addArgument(new KothShowArgument());
		addArgument(this.kothScheduleArgument = new KothScheduleArgument(plugin));
		addArgument(new KothSetCapDelayArgument(plugin));
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			this.kothScheduleArgument.onCommand(sender, command, label, args);
			return true;
		}
		return super.onCommand(sender, command, label, args);
	}
}
