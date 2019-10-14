package com.zdev.hcf.reclaim;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.util.Color;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ReclaimCommand implements CommandExecutor {

	private ReclaimManager rm;

	public ReclaimCommand(ReclaimManager rm) {
		this.rm = rm;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		Player player = (Player) sender;
		if (rm.hasReclaimed(player)) {
			player.sendMessage(Color.translate("&cYou have already reclaimed!"));
			return false;
		}
		String rank = PermissionsEx.getPermissionManager().getUser(player).getGroupNames()[0];
		if (rm.getReclaim(rank).size() == 0) {
			player.sendMessage(
					Color.translate("&cYour rank's reclaim hasn't been setup yet. Please contact an admin."));
			return false;
		}
		rm.setReclaimed(player, true);
		for (String command : rm.getReclaim(rank)) {
			command = command.replace("%PLAYER%", player.getName()).replace("%RANK%", rank);

			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		}
		player.sendMessage(Color.translate("&6&lYou have reclaimed your rank."));

		return false;
	}

}
