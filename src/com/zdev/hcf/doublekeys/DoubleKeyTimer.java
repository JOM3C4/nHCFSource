package com.zdev.hcf.doublekeys;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.zdev.hcf.Base;
import com.zdev.hcf.timer.GlobalTimer;
import com.zdev.hcf.timer.event.TimerExpireEvent;

import net.md_5.bungee.api.ChatColor;

public class DoubleKeyTimer extends GlobalTimer implements Listener {

	private final Base plugin;

	public DoubleKeyTimer(Base plugin) {
		super("DoubleKey", TimeUnit.SECONDS.toMillis(1L));
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onExpire(TimerExpireEvent event) {
		if (event.getTimer() == this) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
					"broadcast &7The store-wide &4&lDoubleKey Sale! &7has ended.");
		}
	}

	@Override
	public String getScoreboardPrefix() {
		return ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString();
	}

}
