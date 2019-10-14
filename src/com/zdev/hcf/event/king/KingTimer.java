package com.zdev.hcf.event.king;

import org.bukkit.ChatColor;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.zdev.hcf.Base;
import com.zdev.hcf.timer.GlobalTimer;
import com.zdev.hcf.timer.event.TimerExpireEvent;

public class KingTimer extends GlobalTimer implements Listener {
	private final Base plugin;

	public KingTimer(final Base plugin) {
		super("Key-All", TimeUnit.SECONDS.toMillis(1L));
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onExpire(final TimerExpireEvent event) {
		if (event.getTimer() == this) {
			Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
					"broadcast &7The &a&lKeyAll &7has happened.");
			Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
					"cr giveallkey " + KingCommand.player + " 1");
		}
	}

	public String getScoreboardPrefix() {
		return String.valueOf(String.valueOf(ChatColor.GOLD.toString())) + ChatColor.BOLD.toString();
	}
}
