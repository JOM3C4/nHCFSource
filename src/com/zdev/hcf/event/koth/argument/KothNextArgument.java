package com.zdev.hcf.event.koth.argument;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.zdev.hcf.Base;
import com.zdev.hcf.DateTimeFormats;
import com.zdev.hcf.util.command.CommandArgument;

public class KothNextArgument extends CommandArgument {
	private final Base plugin;

	public KothNextArgument(Base plugin) {
		super("next", "View the next scheduled KOTH");
		this.plugin = plugin;
		this.permission = ("hcf.command.koth.argument." + getName());
	}

	public String getUsage(String label) {
		return '/' + label + ' ' + getName();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		long millis = System.currentTimeMillis();
		sender.sendMessage(ChatColor.YELLOW + "The current server time is " + ChatColor.GREEN
				+ DateTimeFormats.DAY_MTH_HR_MIN_AMPM.format(millis) + ChatColor.GOLD + '.');
		Map<LocalDateTime, String> scheduleMap = this.plugin.eventScheduler.getScheduleMap();
		if (scheduleMap.isEmpty()) {
			sender.sendMessage(ChatColor.RED + "There is not an event schedule for after now.");
			return true;
		}
		LocalDateTime now = LocalDateTime.now(DateTimeFormats.SERVER_ZONE_ID);
		for (Map.Entry<LocalDateTime, String> entry : scheduleMap.entrySet()) {
			LocalDateTime scheduleDateTime = (LocalDateTime) entry.getKey();
			if (!now.isAfter(scheduleDateTime)) {
				int currentDay = now.getDayOfYear();
				String eventName = (String) entry.getValue();
				int dayDifference = scheduleDateTime.getDayOfYear() - currentDay;
				String monthName = scheduleDateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
				String weekName = scheduleDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
				ChatColor colour = dayDifference == 0 ? ChatColor.GREEN : ChatColor.RED;
				sender.sendMessage(
						"  " + colour + WordUtils.capitalizeFully(eventName) + ChatColor.GRAY + " is the next event: "
								+ ChatColor.YELLOW + weekName + ' ' + scheduleDateTime.getDayOfMonth() + ' ' + monthName
								+ ChatColor.GREEN + " (" + KothScheduleArgument.HHMMA.format(scheduleDateTime) + ')');
				return true;
			}
		}
		sender.sendMessage(ChatColor.RED + "There is not an event scheduled after now.");
		return true;
	}
}
