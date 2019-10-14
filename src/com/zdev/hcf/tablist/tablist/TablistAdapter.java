package com.zdev.hcf.tablist.tablist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import com.zdev.hcf.Base;
import com.zdev.hcf.ConfigurationService;
import com.zdev.hcf.event.faction.EventFaction;
import com.zdev.hcf.event.faction.KothFaction;
import com.zdev.hcf.faction.FactionMember;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.tablist.TablistEntrySupplier;
import com.zdev.hcf.util.CC;
import com.zdev.hcf.util.Color;
import com.zdev.hcf.util.chat.SymbolUtil;

import net.minecraft.util.com.google.common.collect.HashBasedTable;
import net.minecraft.util.com.google.common.collect.Table;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;

public class TablistAdapter implements TablistEntrySupplier {

	public static final Comparator<PlayerFaction> FACTION_COMPARATOR = (faction1, faction2) -> {
		return Integer.compare(faction1.getOnlinePlayers().size(), faction2.getOnlinePlayers().size());
	};

	public static final Comparator<FactionMember> ROLE_COMPARATOR = (member1, member2) -> {
		return Integer.compare(member1.getRole().ordinal(), member2.getRole().ordinal());
	};

	private final Base plugin;

	public TablistAdapter(Base plugin) {
		this.plugin = plugin;
	}

	public static String LINE;

	static {
		TablistAdapter.LINE = "&m&l---------------";
	}

	@Override
	public Table<Integer, Integer, String> getEntries(final Player player) {
		final Table<Integer, Integer, String> table = HashBasedTable.create();
		final PlayerFaction playerFaction = Base.getPlugin().getFactionManager().getPlayerFaction(player.getUniqueId());
		table.put(0, 0, Color.translate("&7" + TablistAdapter.LINE));
		table.put(0, 1, CC.translate(""));
		table.put(0, 2, CC.translate(String.valueOf(ConfigurationService.TAB_COLOR) + "Faction Info"));
		if (playerFaction != null) {
			table.put(0, 3, CC.translate("&7Online: &f" + playerFaction.getOnlineMembers().size() + "/"
					+ playerFaction.getMembers().size()));
			table.put(0, 4, CC.translate("&7DTR: &f" + playerFaction.getDeathsUntilRaidable()));
			table.put(0, 5, CC.translate("&7Balance: &f$" + playerFaction.getBalance()));
			if (playerFaction.getHome() != null) {
				final int locx = (int) playerFaction.getHome().getX();
				final int locz = (int) playerFaction.getHome().getZ();
				table.put(0, 6, CC.translate("&7HQ: &f" + locx + ", " + locz));
			} else {
				table.put(0, 6, CC.translate("&7HQ: &fNone"));
			}
		} else {
			table.put(0, 3, CC.translate("&7You do not"));
			table.put(0, 4, CC.translate("&7have a faction"));
			table.put(0, 5, CC.translate("&7/f create <name>"));
			table.put(0, 6, CC.translate(""));
		}
		table.put(0, 7, CC.translate(""));
		table.put(0, 8, CC.translate(String.valueOf(ConfigurationService.TAB_COLOR) + "Player Info"));
		table.put(0, 9, CC.translate("&7Kills: &f" + player.getStatistic(Statistic.PLAYER_KILLS)));
		table.put(0, 10, CC.translate("&7Deaths: &f" + player.getStatistic(Statistic.DEATHS)));
		table.put(0, 11,
				CC.translate("&7Balance: &f" + Base.getPlugin().getEconomyManager().getBalance(player.getUniqueId())));
		if (ConfigurationService.KIT_MAP) {
			table.put(0, 12, CC.translate(""));
		} else {
			table.put(0, 12,
					CC.translate("&7Lives: &f" + Base.getPlugin().getDeathbanManager().getLives(player.getUniqueId())
							+ " &c" + SymbolUtil.UNICODE_HEART));
		}
		table.put(0, 13, CC.translate(""));
		table.put(0, 14, CC.translate(String.valueOf(ConfigurationService.TAB_COLOR) + "Location Info"));
		table.put(0, 15,
				CC.translate("&7" + (int) player.getLocation().getX() + ", " + (int) player.getLocation().getZ()));
		table.put(0, 16, CC.translate("&7" + TablistAdapter.getCardinalDirection(player)));
		table.put(0, 17, CC.translate(new StringBuilder().append(Base.getPlugin().getFactionManager()
				.getFactionAt(player.getLocation()).getDisplayName((CommandSender) player)).toString()));
		table.put(0, 18, CC.translate(""));
		table.put(0, 19, CC.translate("&7" + TablistAdapter.LINE));
		table.put(1, 0, CC.translate("&7" + TablistAdapter.LINE));
		table.put(1, 1, CC.translate(String.valueOf(ConfigurationService.TAB_COLOR) + ConfigurationService.TAB_TITLE));
		table.put(1, 2, CC.translate("&7" + Bukkit.getOnlinePlayers().length + "/" + Bukkit.getMaxPlayers()));
		table.put(1, 3, CC.translate(""));
		if (playerFaction != null) {
			table.put(1, 4, CC.translate("&a&l" + playerFaction.getDisplayName((CommandSender) player) + "&7:"));
			final List<FactionMember> members = new ArrayList<FactionMember>(playerFaction.getMembers().values()
					.stream().filter(member -> Bukkit.getPlayer(member.getUniqueId()) != null)
					.collect(Collectors.toList()));
			Collections.sort(members, TablistAdapter.ROLE_COMPARATOR);
			for (int i = 5; i < 20; ++i) {
				final int exact = i - 5;
				if (members.size() > exact) {
					if (i == 19 && members.size() > 19) {
						table.put(1, i, CC.translate("&7 and " + (members.size() - 19) + " more..."));
					} else {
						final FactionMember targetMember = members.get(exact);
						table.put(1, i, CC
								.translate("&a" + targetMember.getName() + "&7" + targetMember.getRole().getAstrix()));
					}
				}
			}
		} else {
			table.put(1, 4, CC.translate(""));
			table.put(1, 5, CC.translate(""));
			table.put(1, 6, CC.translate(""));
			table.put(1, 7, CC.translate(""));
			table.put(1, 8, CC.translate(""));
			table.put(1, 9, CC.translate(""));
			table.put(1, 10, CC.translate(""));
			table.put(1, 11, CC.translate(""));
			table.put(1, 12, CC.translate(""));
			table.put(1, 13, CC.translate(""));
			table.put(1, 14, CC.translate(""));
			table.put(1, 15, CC.translate(""));
			table.put(1, 16, CC.translate(""));
			table.put(1, 17, CC.translate(""));
			table.put(1, 18, CC.translate(""));
		}
		table.put(1, 19, CC.translate("&7" + TablistAdapter.LINE));
		table.put(2, 0, CC.translate("&7" + TablistAdapter.LINE));
		table.put(2, 1, CC.translate(""));
		table.put(2, 2, CC.translate(String.valueOf(ConfigurationService.TAB_COLOR) + "TeamSpeak"));
		table.put(2, 3, CC.translate("&7" + ConfigurationService.TEAMSPEAK_IP));
		table.put(2, 4, CC.translate(""));
		table.put(2, 5, CC.translate(String.valueOf(ConfigurationService.TAB_COLOR) + "Twitter"));
		table.put(2, 6, CC.translate("&7" + ConfigurationService.TWITTER));
		table.put(2, 7, CC.translate(""));
		table.put(2, 8, CC.translate(String.valueOf(ConfigurationService.TAB_COLOR) + "Store"));
		table.put(2, 9, CC.translate("&7" + ConfigurationService.STORE));
		table.put(2, 10, CC.translate(""));
		table.put(2, 11, CC.translate(String.valueOf(ConfigurationService.TAB_COLOR) + "Map Info"));
		table.put(2, 12,
				CC.translate("&7Sharp: &f" + ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.DAMAGE_ALL)));
		table.put(2, 13, CC.translate(
				"&7Prot: &f" + ConfigurationService.ENCHANTMENT_LIMITS.get(Enchantment.PROTECTION_ENVIRONMENTAL)));
		table.put(2, 14, CC.translate("&7Border: &f" + ConfigurationService.TAB_BORDER));
		table.put(2, 15, CC.translate(""));
		table.put(2, 16, CC.translate(String.valueOf(ConfigurationService.TAB_COLOR) + "End Portals"));
		table.put(2, 17, CC.translate(
				ConfigurationService.KIT_MAP ? "&7In Spawn" : ("&7" + ConfigurationService.TAB_END_PORTALS)));
		table.put(2, 18, CC.translate(
				new StringBuilder().append(ConfigurationService.KIT_MAP ? "" : "&7in each quadrant").toString()));
		table.put(2, 19, CC.translate("&7" + TablistAdapter.LINE));
		return table;
	}

	public static String getCardinalDirection(Player player) {
		double rotation = (player.getLocation().getYaw() + 180F) % 360.0F;
		if (rotation < 0.0D) {
			rotation += 360.0D;
		}
		if ((0.0D <= rotation) && (rotation < 22.5D)) {
			return "N";
		}
		if ((22.5D <= rotation) && (rotation < 67.5D)) {
			return "NE";
		}
		if ((67.5D <= rotation) && (rotation < 112.5D)) {
			return "E";
		}
		if ((112.5D <= rotation) && (rotation < 157.5D)) {
			return "SE";
		}
		if ((157.5D <= rotation) && (rotation < 202.5D)) {
			return "S";
		}
		if ((202.5D <= rotation) && (rotation < 247.5D)) {
			return "SW";
		}
		if ((247.5D <= rotation) && (rotation < 292.5D)) {
			return "W";
		}
		if ((292.5D <= rotation) && (rotation < 337.5D)) {
			return "NW";
		}
		if ((337.5D <= rotation) && (rotation < 360.0D)) {
			return "N";
		}
		return null;
	}

	public String[] getKothInfo() {
		String[] info = new String[] { ConfigurationService.SECONDAIRY_COLOR + "Next Event",
				ChatColor.GRAY + "None Event", "" };
		EventFaction eventFaction = plugin.getTimerManager().getEventTimer().getEventFaction();
		if (eventFaction instanceof KothFaction) {
			KothFaction faction = (KothFaction) eventFaction;
			Location center = faction.getCaptureZone().getCuboid().getCenter();
			info = new String[] { ConfigurationService.SECONDAIRY_COLOR + faction.getName(),
					ChatColor.GRAY + DurationFormatUtils
							.formatDuration(plugin.getTimerManager().getEventTimer().getRemaining(), "mm:ss"),
					ChatColor.GRAY.toString() + center.getBlockX() + ", " + center.getBlockZ(), };
		}
		return info;
	}

	@Override
	public String getHeader(Player player) {
		return ConfigurationService.YELLOW + "You are playing " + ConfigurationService.NAME;
	}

	@Override
	public String getFooter(Player player) {
		return "";
	}

}
