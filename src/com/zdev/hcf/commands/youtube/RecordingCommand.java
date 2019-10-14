package com.zdev.hcf.commands.youtube;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.util.BukkitUtils;
import com.zdev.hcf.util.Messager;

public class RecordingCommand implements CommandExecutor {

	private Map<String, Boolean> recording = new HashMap<String, Boolean>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] argument) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players!");
			return true;
		}
		Player player = (Player) sender;

		if (this.recording.containsKey(player.getName())) {

			Messager.broadcast(BukkitUtils.STRAIGHT_LINE_DEFAULT);
			Messager.broadcast("");
			Messager.broadcast("&6" + player.getName() + " &7has stopped &crecording&7!");
			Messager.broadcast("");
			Messager.broadcast(BukkitUtils.STRAIGHT_LINE_DEFAULT);

			this.recording.remove(player.getName());
		} else {

			Messager.broadcast(BukkitUtils.STRAIGHT_LINE_DEFAULT);
			Messager.broadcast("");
			Messager.broadcast("&6" + player.getName() + " &7has started &crecording&7!");
			Messager.broadcast("");
			Messager.broadcast(BukkitUtils.STRAIGHT_LINE_DEFAULT);

			Messager.player(player, "&4&lALERT: &cSpamming with this command, may result a PUNISHMENT.");
			this.recording.put(player.getName(), true);
		}

		return false;
	}

}
