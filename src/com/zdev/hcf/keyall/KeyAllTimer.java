package com.zdev.hcf.keyall;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.zdev.hcf.Base;
import com.zdev.hcf.timer.GlobalTimer;
import com.zdev.hcf.timer.event.TimerExpireEvent;

import net.md_5.bungee.api.ChatColor;

public class KeyAllTimer extends GlobalTimer implements Listener {

	private final Base plugin;

	public KeyAllTimer(Base plugin) {
		super("Key All", TimeUnit.SECONDS.toMillis(1L));
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onExpire(TimerExpireEvent event) {
		if (event.getTimer() == this) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "broadcast &7The key-all has happened.");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mcrate giveall " + "March" + "1");
		}
	}

	@Override
	public String getScoreboardPrefix() {
		return ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString();
	}

}
