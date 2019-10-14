package com.zdev.hcf.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;
import com.zdev.hcf.Cooldowns;

public class HelpOpCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			System.out.println("Players only.");
		} else {
			Player p = (Player) sender;
			if (args.length == 0) {
				p.sendMessage(("§cUsage: /helpop <message>"));
				return true;
			}
			final Player player = (Player) sender;
			if (Cooldowns.isOnCooldown("helpop_cooldown", player)) {
				sender.sendMessage("§cYou cannot do this for another §l"
						+ Cooldowns.getCooldownForPlayerInt("helpop_cooldown", player) / 1 + " §cseconds.");
				return true;
			}

			String message = Joiner.on(' ').join(args);
			for (Player p1 : Bukkit.getOnlinePlayers()) {
				if (p1.hasPermission("core.staff")) {
					p1.sendMessage(("§7(§3HelpOp§7) §b" + p.getName() + " §7»§b " + message));
				}
			}

			p.sendMessage(("§aYour request has been sent to all online staff members."));
			Cooldowns.addCooldown("helpop_cooldown", player, 60);

		}
		return false;
	}

}
