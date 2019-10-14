package com.zdev.hcf.listener.fixes;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherFixListener implements Listener {
	@EventHandler
	public void onWeatherChange(final WeatherChangeEvent e) {
		if (e.getWorld().getEnvironment() == World.Environment.NORMAL && e.toWeatherState()) {
			e.setCancelled(true);
		}
	}
}