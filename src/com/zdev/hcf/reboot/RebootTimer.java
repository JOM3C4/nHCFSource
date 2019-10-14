package com.zdev.hcf.reboot;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.zdev.hcf.Base;
import com.zdev.hcf.timer.GlobalTimer;
import com.zdev.hcf.timer.event.TimerExpireEvent;

import net.md_5.bungee.api.ChatColor;

public class RebootTimer extends GlobalTimer implements Listener {

	private final Base plugin;

	public RebootTimer(Base plugin) {
		super("Reboot", TimeUnit.SECONDS.toMillis(1L));
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onExpire(TimerExpireEvent event) {
		if (event.getTimer() == this) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
		}
	}

	@Override
	public String getScoreboardPrefix() {
		return ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString();
	}

}
