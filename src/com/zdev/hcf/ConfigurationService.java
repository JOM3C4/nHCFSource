package com.zdev.hcf;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.MemorySection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionType;

import com.google.common.collect.ImmutableList;

import gnu.trove.impl.Constants;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

public final class ConfigurationService {
	public static final TimeZone SERVER_TIME_ZONE = TimeZone.getTimeZone("EST");
	public static final int WARZONE_RADIUS = Base.config.getInt("WARZONE_RADIUS");
	public static final int SPAWN_NO_BREAK_RADIUS = Base.config.getInt("SPAWN_NO_BREAK_RADIUS");
	public static final int NETHER_WARZONE_RADIUS = Base.config.getInt("NETHER_WARZONE_RADIUS");
	public static final String IP = Base.config.getString("Footer.Footer-Text");
	public static final String TEAMSPEAK_URL = Base.config.getString("teamspeak-frozen");
	public static final String BLOCKED_POTIONS = Base.config.getString("blocked-potions");
	public static final String DONATE_URL = Base.config.getString("DONATE_URL");
	public static final String TOP_RANK = Base.config.getString("top-rank");
	public static String EGGTELEPORT_TIMER;
	public static final String SUBREDDIT_URL = "Development";
	public static final int SPAWN_BUFFER = 100;
	public static final double MAP_NUMBER = 1.0D;
	@SuppressWarnings("static-access")
	public static final boolean TAB_LIST = Base.getPlugin().config.getBoolean("tab-list");
	public static final boolean SBN = Base.getPlugin().config.getBoolean("Scoreboard.Staffboard.Permit");
	public static final boolean SBV = Base.getPlugin().config.getBoolean("Scoreboard.Vanish.Permit");
	public static final boolean SBSC = Base.getPlugin().config.getBoolean("Scoreboard.StaffChat.Permit");
	public static final boolean SBOP = Base.getPlugin().config.getBoolean("Scoreboard.OnlinePlayers.Permit");
	public static final boolean SBGM = Base.getPlugin().config.getBoolean("Scoreboard.Gamemode.Permit");
	public static final boolean KIT_MAP = Base.getPlugin().config.getBoolean("kits-mode");
	public static final boolean COMBAT_BUILDING = Base.getPlugin().config.getBoolean("block-in-combat");
//	public static final boolean GET_POTS_FREE = Base.getPlugin().config.getBoolean("get_pots_free");
	public static final List<String> DISALLOWED_FACTION_NAMES = ImmutableList.of("shit", "niggar", "nigger", "shit",
			"kohieotw", "kohisotw", "hcteams", "hcteamseotw", "hcteamssotw");
	private static final TObjectIntMap<PotionType> POTION_LIMITS = new TObjectIntHashMap<>(Constants.DEFAULT_CAPACITY,
			Constants.DEFAULT_LOAD_FACTOR, -1);
	private final static List<PotionType> disallowedExtendedPotions = new ArrayList<>();
	public static final Map<Enchantment, Integer> ENCHANTMENT_LIMITS = new HashMap<Enchantment, Integer>();
	public static final Map<Enchantment, Integer> KOTH_LIMITS = new HashMap<Enchantment, Integer>();
	public static final Map<Enchantment, Integer> TIER1_LIMITS = new HashMap<Enchantment, Integer>();
	public static final Map<Enchantment, Integer> TIER3_LIMITS = new HashMap<Enchantment, Integer>();
	public static final Map<Enchantment, Integer> TIER2_LIMITS = new HashMap<Enchantment, Integer>();
	public static final Map<World.Environment, Double> SPAWN_RADIUS_MAP = new EnumMap<Environment, Double>(
			World.Environment.class);
	public static double EXP_MULTIPLIER_GENERAL = 2.0D;
	public static double EXP_MULTIPLIER_FISHING = 2.0D;
	public static double EXP_MULTIPLIER_SMELTING = 2.0D;
	public static double EXP_MULTIPLIER_LOOTING_PER_LEVEL = 2.0D;
	public static double EXP_MULTIPLIER_LUCK_PER_LEVEL = 2.0D;
	public static double EXP_MULTIPLIER_FORTUNE_PER_LEVEL = 2.0D;
	private boolean handleCombatLogging = true;

	public static final String PRIMAIRY_COLOR = Base.getPlugin().getConfig().getString("Colours.PRIMARY-COLOR")
			.replace("&", "§");
	public static final String SECONDAIRY_COLOR = Base.getPlugin().getConfig().getString("Colours.SECONDARY-COLOR")
			.replace("&", "§");
	public static final String TIMER_COLOR = Base.getPlugin().getConfig().getString("Colours.TIMER-COLOR").replace("&",
			"§");

	public boolean isHandleCombatLogging() {
		return this.handleCombatLogging;
	}

	static {

		Object object = null;

		if ((object = Base.config.get("potionLimits")) != null && object instanceof MemorySection) {
			MemorySection section = (MemorySection) object;

			for (String key : section.getKeys(false)) {
				PotionType type = null;

				try {
					type = PotionType.valueOf(key);
					Integer level = section.getInt(key);

					POTION_LIMITS.put(type, level);

					System.out.println("Found potion limit " + type.name() + "#" + level);
				} catch (IllegalArgumentException exception) {
					exception.printStackTrace();
					continue;
				}
			}
		}

		ENCHANTMENT_LIMITS.put(Enchantment.PROTECTION_ENVIRONMENTAL,
				Integer.valueOf(Base.config.getInt("Kitmap.Protection")));
		KOTH_LIMITS.put(Enchantment.PROTECTION_ENVIRONMENTAL, Integer.valueOf(2));
		TIER1_LIMITS.put(Enchantment.PROTECTION_ENVIRONMENTAL, Integer.valueOf(0));
		TIER2_LIMITS.put(Enchantment.PROTECTION_ENVIRONMENTAL, Integer.valueOf(1));
		TIER3_LIMITS.put(Enchantment.PROTECTION_ENVIRONMENTAL, Integer.valueOf(2));

		ENCHANTMENT_LIMITS.put(Enchantment.DAMAGE_ALL, Integer.valueOf(Base.config.getInt("Kitmap.Sharpness")));
		ENCHANTMENT_LIMITS.put(Enchantment.ARROW_KNOCKBACK, Integer.valueOf(0));
		ENCHANTMENT_LIMITS.put(Enchantment.KNOCKBACK, Integer.valueOf(0));
		ENCHANTMENT_LIMITS.put(Enchantment.FIRE_ASPECT, Integer.valueOf(0));
		ENCHANTMENT_LIMITS.put(Enchantment.THORNS, Integer.valueOf(0));
		ENCHANTMENT_LIMITS.put(Enchantment.ARROW_FIRE, Integer.valueOf(1));
		ENCHANTMENT_LIMITS.put(Enchantment.ARROW_DAMAGE, Integer.valueOf(3));

		SPAWN_RADIUS_MAP.put(World.Environment.NORMAL, Double.valueOf(0.0D));
		SPAWN_RADIUS_MAP.put(World.Environment.NETHER, Double.valueOf(0.0D));
		SPAWN_RADIUS_MAP.put(World.Environment.THE_END, Double.valueOf(0.0D));
	}

	public static final ChatColor TEAMMATE_COLOUR = ChatColor.valueOf(Base.config.getString("TEAMMATE_COLOUR"));
	public static final ChatColor ALLY_COLOUR = ChatColor.valueOf(Base.config.getString("ALLY_COLOUR"));
	public static final ChatColor ENEMY_COLOUR = ChatColor.valueOf(Base.config.getString("ENEMY_COLOUR"));
	public static final ChatColor SAFEZONE_COLOUR = ChatColor.valueOf(Base.config.getString("SAFEZONE_COLOUR"));
	public static final ChatColor ROAD_COLOUR = ChatColor.valueOf(Base.config.getString("ROAD_COLOUR"));
	public static final ChatColor WARZONE_COLOUR = ChatColor.valueOf(Base.config.getString("WARZONE_COLOUR"));
	public static final ChatColor WILDERNESS_COLOUR = ChatColor.valueOf(Base.config.getString("WILDERNESS_COLOUR"));
	public static final ChatColor GLOWSTONE_COLOUR = ChatColor.valueOf(Base.config.getString("GLOWSTONE_COLOUR"));
	public static final ChatColor FOCUS_COLOUR = ChatColor.LIGHT_PURPLE;
	public static ChatColor WHITE = ChatColor.WHITE;
	public static ChatColor GREEN = ChatColor.GREEN;
	public static ChatColor RED = ChatColor.RED;
	public static ChatColor GOLD = ChatColor.GOLD;
	public static ChatColor YELLOW = ChatColor.YELLOW;
	public static ChatColor GRAY = ChatColor.GRAY;
	public static ChatColor EGGTELEPORT_COLOUR;
	public static final String SCOREBOARD_TITLE = ChatColor.translateAlternateColorCodes('&',
			Base.config.getString("Scoreboard_title"));

	public static final int MAX_ALLIES_PER_FACTION = Base.config.getInt("MAX-ALLY");
	public static final int MAX_PLAYER_PER_FACTION = Base.config.getInt("MAX-FACTION");
	public static final int CONQUEST_REQUIRED_WIN_POINTS = 150;
	public static int DEFAULT_DEATHBAN_DURATION = Base.config.getInt("DEFAULT_DEATHBAN_DURATION");

	public static final long DTR_MILLIS_BETWEEN_UPDATES = TimeUnit.SECONDS.toMillis(45L);
	public static final String DTR_WORDS_BETWEEN_UPDATES = DurationFormatUtils
			.formatDurationWords(DTR_MILLIS_BETWEEN_UPDATES, true, true);
	@SuppressWarnings("static-access")
	public static boolean CRATE_BROADCASTS = false;

	// INFO
	public static final String TWITTER = Base.config.getString("SERVER-INFO.TWITTER");
	public static final String TEAMSPEAK_IP = Base.config.getString("SERVER-INFO.TEAMSPEAK");
	public static final String STORE = Base.config.getString("SERVER-INFO.STORE");
	public static final String NAME = Base.config.getString("SERVER-INFO.SERVER_NAME");
	// INFO
	// Tab
	public static final String TAB_BORDER = Base.config.getString("TAB.BORDER").replace("&", "§");
	public static final String TAB_TITLE = Base.config.getString("TAB.TITLE").replace("&", "§");
	public static final String TAB_COLOR = Base.config.getString("TAB.COLOR").replace("&", "§");
	public static final String TAB_END_PORTALS = Base.config.getString("TAB.END-PORTALS");
	public static final boolean TAB = Base.config.getBoolean("TAB.ENABLE");
	// Tab
	private int combatlogDespawnDelayTicks = Base.config.getInt("COMBATLOG_DESPAWN_DELAY_TICKS");

	public int getCombatlogDespawnDelayTicks() {
		return this.combatlogDespawnDelayTicks;
	}

	public static int getPotionLimit(PotionType potionEffectType) {
		int maxLevel = POTION_LIMITS.get(potionEffectType);
		return maxLevel == POTION_LIMITS.getNoEntryValue() ? potionEffectType.getMaxLevel() : maxLevel;
	}

	public static boolean isExtendedDurationDisallowed(PotionType type) {
		return disallowedExtendedPotions.contains(type);
	}

	public String getString(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getStringList(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
