package com.zdev.hcf.scoreboard.provider;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.ConfigurationService;
import com.zdev.hcf.DateTimeFormats;
import com.zdev.hcf.classes.PvpClass;
import com.zdev.hcf.classes.archer.ArcherClass;
import com.zdev.hcf.classes.bard.BardClass;
import com.zdev.hcf.classes.type.MinerClass;
import com.zdev.hcf.classes.type.RogueClass;
import com.zdev.hcf.commands.StaffModeCommand;
import com.zdev.hcf.event.EventTimer;
import com.zdev.hcf.event.eotw.EOTWHandler;
import com.zdev.hcf.event.faction.ConquestFaction;
import com.zdev.hcf.event.faction.EventFaction;
import com.zdev.hcf.event.king.KingCommand;
import com.zdev.hcf.event.tracker.ConquestTracker;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.listener.EggSwapp;
import com.zdev.hcf.listener.StaffChatListener;
import com.zdev.hcf.listener.VanishListener;
import com.zdev.hcf.reboot.RebootTimer;
import com.zdev.hcf.scoreboard.SidebarEntry;
import com.zdev.hcf.scoreboard.SidebarProvider;
import com.zdev.hcf.sotw.SotwCommand;
import com.zdev.hcf.sotw.SotwTimer;
import com.zdev.hcf.specialitems.GrappleListener;
import com.zdev.hcf.timer.GlobalTimer;
import com.zdev.hcf.timer.PlayerTimer;
import com.zdev.hcf.timer.Timer;
import com.zdev.hcf.timer.type.SnowBall;
import com.zdev.hcf.timer.type.TeleportTimer;
import com.zdev.hcf.util.BoardUtils;
import com.zdev.hcf.util.BukkitUtils;
import com.zdev.hcf.util.CC;
import com.zdev.hcf.util.DurationFormatter;

public class TimerSidebarProvider implements SidebarProvider {

	protected static String STRAIGHT_LINE = BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 14);
	protected static final String NEW_LINE = ChatColor.STRIKETHROUGH + "----------";

	private Base plugin;

	public TimerSidebarProvider(Base plugin) {
		this.plugin = plugin;
	}

	private static String handleBardFormat(long millis, boolean trailingZero) {
		return ((DecimalFormat) (trailingZero ? DateTimeFormats.REMAINING_SECONDS_TRAILING
				: DateTimeFormats.REMAINING_SECONDS).get()).format(millis * 0.001D);
	}

	public SidebarEntry add(String s) {

		if (s.length() < 10) {
			return new SidebarEntry(s);
		}

		if (s.length() > 10 && s.length() < 20) {
			return new SidebarEntry(s.substring(0, 10), s.substring(10, s.length()), "");
		}

		if (s.length() > 20) {
			return new SidebarEntry(s.substring(0, 10), s.substring(10, 20), s.substring(20, s.length()));
		}

		return null;
	}

	@Override
	public String getTitle() {
		return ConfigurationService.SCOREBOARD_TITLE;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public List<SidebarEntry> getLines(Player player) {

		List<SidebarEntry> lines = new ArrayList<SidebarEntry>();
		EOTWHandler.EotwRunnable eotwRunnable = this.plugin.getEotwHandler().getRunnable();
		PvpClass pvpClass = this.plugin.getPvpClassManager().getEquippedClass(player);
		EventTimer eventTimer = this.plugin.getTimerManager().eventTimer;
		List<SidebarEntry> conquestLines = null;
		Collection<Timer> timers = this.plugin.getTimerManager().getTimers();
		EventFaction eventFaction = eventTimer.getEventFaction();

		if ((ConfigurationService.KIT_MAP) == true) {
			lines.add(new SidebarEntry("§8» §e§lStats"));
			lines.add(this.add(Base.getPlugin().getConfig().getString("Scoreboard.Kills.Name")
					.replace("%kills%", String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS)))
					.replaceAll("&", "§")));
			lines.add(this.add(Base.getPlugin().getConfig().getString("Scoreboard.Deaths.Name")
					.replace("%deaths%", String.valueOf(player.getStatistic(Statistic.DEATHS))).replaceAll("&", "§")));

		}
		StaffModeCommand.getInstance();
		if (StaffModeCommand.isMod(player)) {
			lines.add(new SidebarEntry("§6§lStaff Mode"));
			final List<SidebarEntry> list = lines;
			final String prefix = "§8» §eVan";
			final String name = "ished§7: ";
			VanishListener.getInstance();
			list.add(new SidebarEntry(prefix, name,
					VanishListener.isVanished(player) ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled")));
			lines.add(new SidebarEntry("§8» §eStaff", " Chat§7: ",
					StaffChatListener.getInstance().isInStaffChat(player) ? (ChatColor.GREEN + "Enabled")
							: (ChatColor.RED + "Disabled")));
			lines.add(new SidebarEntry("§8» §ePlay", "ers§7: §c", Bukkit.getOnlinePlayers().length));
		}
		{
		}

		if ((pvpClass != null) && ((pvpClass instanceof BardClass))) {
			BardClass bardClass = (BardClass) pvpClass;
			lines.add(new SidebarEntry(ChatColor.AQUA + ChatColor.BOLD.toString() + "Bard ",
					ChatColor.AQUA + ChatColor.BOLD.toString() + "Energy",
					ChatColor.GRAY + ": " + ChatColor.RED + handleBardFormat(bardClass.getEnergyMillis(player), true)));
			long remaining2 = bardClass.getRemainingBuffDelay(player);
			if (remaining2 > 0L) {
				lines.add(new SidebarEntry(ChatColor.GREEN + ChatColor.BOLD.toString() + "Bard ",
						ChatColor.GREEN + ChatColor.BOLD.toString() + "Effect",
						ChatColor.GRAY + ": " + ChatColor.RED + Base.getRemaining(remaining2, true)));
			}
		}
		final SotwTimer.SotwRunnable sotwRunnable = this.plugin.getSotwTimer().getSotwRunnable();
		if (sotwRunnable != null) {
			if (SotwCommand.enabled.contains(player.getUniqueId())) {
				lines.add(new SidebarEntry(String.valueOf(ChatColor.GRAY.toString()), "§a§lSOTW",
						ChatColor.GRAY + ": " + String.valueOf(((ConfigurationService.TIMER_COLOR)))
								+ ChatColor.STRIKETHROUGH + Base.getRemaining(sotwRunnable.getRemaining(), true)));

			} else {
				lines.add(new SidebarEntry(String.valueOf(ChatColor.GRAY.toString()), "§a§lSOTW",
						ChatColor.GRAY + ": " + String.valueOf(((ConfigurationService.TIMER_COLOR)))
								+ Base.getRemaining(sotwRunnable.getRemaining(), true)));
			}
		}

		if ((pvpClass instanceof MinerClass)) {
			lines.add(new SidebarEntry(ChatColor.GREEN.toString(), "Active Class",
					ChatColor.GRAY + ": " + ChatColor.RED + "Miner"));
		}

		if ((pvpClass instanceof ArcherClass)) {
			lines.add(new SidebarEntry(ChatColor.GREEN.toString(), "Active Class",
					ChatColor.GRAY + ": " + ChatColor.RED + "Archer"));
		}

		if ((pvpClass instanceof BardClass)) {
			lines.add(new SidebarEntry(ChatColor.GREEN.toString(), "Active Class",
					ChatColor.GRAY + ": " + ChatColor.RED + "Bard"));
		}

		if ((pvpClass instanceof RogueClass)) {
			lines.add(new SidebarEntry(ChatColor.GREEN.toString(), "Active Class",
					ChatColor.GRAY + ": " + ChatColor.RED + "Rogue"));
		}

		if (EggSwapp.isOnCooldown(player)) {
			lines.add(new SidebarEntry("§B§lEgg", "§7: §c", EggSwapp.getCooldown(player)));
		}
		if (SnowBall.isOnCooldown(player)) {
			lines.add(new SidebarEntry("§B§lSnow", "Ball§7: §c", SnowBall.getCooldown(player)));
		}
		if (GrappleListener.hasCooldown(player)) {
			lines.add(new SidebarEntry("§6§lGrapple", " Hook§7: §c",
					String.valueOf(GrappleListener.getSeconds(player)) + "s"));
		}

		for (Timer timer : timers) {
			if (((timer instanceof PlayerTimer)) && (!(timer instanceof TeleportTimer))) {
				PlayerTimer playerTimer = (PlayerTimer) timer;
				long remaining3 = playerTimer.getRemaining(player);
				if (remaining3 > 0L) {
					String timerName1 = playerTimer.getName();
					if (timerName1.length() > 14) {
						timerName1 = timerName1.substring(0, timerName1.length());
					}
					lines.add(new SidebarEntry(playerTimer.getScoreboardPrefix(), timerName1 + ChatColor.GRAY,
							": " + String.valueOf(((ConfigurationService.TIMER_COLOR)))
									+ Base.getRemaining(remaining3, true)));
				}
			} else if ((timer instanceof GlobalTimer)) {
				GlobalTimer playerTimer2 = (GlobalTimer) timer;
				long remaining3 = playerTimer2.getRemaining();
				if (remaining3 > 0L) {
					String timerName = playerTimer2.getName();
					if (timerName.length() > 14) {
						timerName = timerName.substring(0, timerName.length());
					}
					if (!timerName.equalsIgnoreCase("Conquest")) {
						lines.add(new SidebarEntry(playerTimer2.getScoreboardPrefix(), timerName + ChatColor.GRAY,
								": " + String.valueOf(((ConfigurationService.TIMER_COLOR)))
										+ Base.getRemaining(remaining3, true)));
					}
				}
			}
		}

		if (eotwRunnable != null) {
			long remaining4 = eotwRunnable.getTimeUntilStarting();
			if (remaining4 > 0L) {
				lines.add(new SidebarEntry(ChatColor.DARK_RED.toString() + ChatColor.BOLD, "EOTW" + ChatColor.GRAY + "",
						"" + ChatColor.GRAY + ": " + ChatColor.RED + Base.getRemaining(remaining4, true)));
			} else if ((remaining4 = eotwRunnable.getTimeUntilCappable()) > 0L) {
				lines.add(new SidebarEntry(ChatColor.DARK_RED.toString() + ChatColor.BOLD, "EOTW" + ChatColor.GRAY + "",
						"" + ChatColor.GRAY + ": " + ChatColor.RED + Base.getRemaining(remaining4, true)));
			}
		}

		if ((eventFaction instanceof ConquestFaction)) {
			ConquestFaction conquestFaction = (ConquestFaction) eventFaction;
			CONQUEST_FORMATTER.get();
			conquestLines = new ArrayList<SidebarEntry>();
			ConquestTracker conquestTracker = (ConquestTracker) conquestFaction.getEventType().getEventTracker();
			int count = 0;
			for (Iterator<?> localIterator = conquestTracker.getFactionPointsMap().entrySet().iterator(); localIterator
					.hasNext(); count = 3) {
				Map.Entry<PlayerFaction, Integer> entry = (Map.Entry) localIterator.next();
				String factionName = ((PlayerFaction) entry.getKey()).getDisplayName(player);
				if (factionName.length() > 14) {
					factionName = factionName.substring(0, 14);
				}
				lines.add(new SidebarEntry(ChatColor.WHITE + " * " + ChatColor.RED, factionName,
						ChatColor.GRAY + ": " + ChatColor.RED + entry.getValue()));
				if (++count == 3) {
					break;
				}
			}
		}
		if (KingCommand.player != null) {
			final Player player2 = Bukkit.getPlayer(KingCommand.kingName);
			lines.add(this.add(ChatColor.translateAlternateColorCodes('&',
					Base.getPlugin().getConfig().getString("SCOREBOARD.KILLEVENT.TITLE").replace("&", "§"))));
			lines.add(this.add(ChatColor.translateAlternateColorCodes('&', Base.getPlugin().getConfig()
					.getString("SCOREBOARD.KILLEVENT.KING").replace("%king%", player2.getName()).replace("&", "§"))));
			lines.add(
					this.add(
							ChatColor
									.translateAlternateColorCodes('&',
											Base.getPlugin().getConfig().getString("SCOREBOARD.KILLEVENT.LOCATION")
													.replace("%location%",
															String.valueOf(player2.getLocation().getBlockX()) + ", "
																	+ player2.getLocation().getBlockZ())
													.replace("&", "§"))));
			lines.add(this.add(ChatColor.translateAlternateColorCodes('&',
					Base.getPlugin().getConfig().getString("SCOREBOARD.KILLEVENT.PRIZE")
							.replace("%prize%", KingCommand.kingPrize).replace("&", "§"))));
		}

		if ((conquestLines != null) && (!conquestLines.isEmpty())) {
			if (player.hasPermission("command.mod")) {
				conquestLines.add(new SidebarEntry("§7§m------", "------", "--------"));
			}
			conquestLines.addAll(lines);
			lines = conquestLines;
		}
		if (!lines.isEmpty()) {
			lines.add(0, new SidebarEntry(ChatColor.GRAY, ChatColor.STRIKETHROUGH + "-----------", "---------"));
			lines.add(lines.size(), new SidebarEntry(ChatColor.GRAY, NEW_LINE, "----------"));
		}
		return lines;

	}

	private String format(double d) {
		// TODO Auto-generated method stub
		return null;
	}

	public static final ThreadLocal<DecimalFormat> CONQUEST_FORMATTER = new ThreadLocal<DecimalFormat>() {
		protected DecimalFormat initialValue() {
			return new DecimalFormat("00.0");
		}
	};
}
