package com.zdev.hcf.tablist;

import org.bukkit.scoreboard.Team;
import net.minecraft.util.com.google.common.collect.Table;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.spigotmc.ProtocolInjector;

import com.zdev.hcf.tablist.reflection.ReflectionConstants;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import java.util.UUID;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.entity.Player;

public class Tablist {
	public static final Object[] GAME_PROFILES;
	public static final String[] TAB_NAMES;
	public static String[] BLANK_SKIN;
	private final ClientVersion version;
	private final Player player;
	private boolean initiated;
	private Scoreboard scoreboard;

	static {
		Tablist.BLANK_SKIN = new String[] {
				"eyJ0aW1lc3RhbXAiOjE0MTEyNjg3OTI3NjUsInByb2ZpbGVJZCI6IjNmYmVjN2RkMGE1ZjQwYmY5ZDExODg1YTU0NTA3MTEyIiwicHJvZmlsZU5hbWUiOiJsYXN0X3VzZXJuYW1lIiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg0N2I1Mjc5OTg0NjUxNTRhZDZjMjM4YTFlM2MyZGQzZTMyOTY1MzUyZTNhNjRmMzZlMTZhOTQwNWFiOCJ9fX0=",
				"u8sG8tlbmiekrfAdQjy4nXIcCfNdnUZzXSx9BE1X5K27NiUvE1dDNIeBBSPdZzQG1kHGijuokuHPdNi/KXHZkQM7OJ4aCu5JiUoOY28uz3wZhW4D+KG3dH4ei5ww2KwvjcqVL7LFKfr/ONU5Hvi7MIIty1eKpoGDYpWj3WjnbN4ye5Zo88I2ZEkP1wBw2eDDN4P3YEDYTumQndcbXFPuRRTntoGdZq3N5EBKfDZxlw4L3pgkcSLU5rWkd5UH4ZUOHAP/VaJ04mpFLsFXzzdU4xNZ5fthCwxwVBNLtHRWO26k/qcVBzvEXtKGFJmxfLGCzXScET/OjUBak/JEkkRG2m+kpmBMgFRNtjyZgQ1w08U6HHnLTiAiio3JswPlW5v56pGWRHQT5XWSkfnrXDalxtSmPnB5LmacpIImKgL8V9wLnWvBzI7SHjlyQbbgd+kUOkLlu7+717ySDEJwsFJekfuR6N/rpcYgNZYrxDwe4w57uDPlwNL6cJPfNUHV7WEbIU1pMgxsxaXe8WSvV87qLsR7H06xocl2C0JFfe2jZR4Zh3k9xzEnfCeFKBgGb4lrOWBu1eDWYgtKV67M2Y+B3W5pjuAjwAxn0waODtEn/3jKPbc/sxbPvljUCw65X+ok0UUN1eOwXV5l2EGzn05t3Yhwq19/GxARg63ISGE8CKw=" };
		GAME_PROFILES = new Object[80];
		TAB_NAMES = new String[80];
		for (int i = 0; i < 80; ++i) {
			final int x = i % 4;
			final int y = i / 4;
			final String name = String
					.valueOf((x > 9)
							? new StringBuilder("§").append(String.valueOf(x).toCharArray()[0]).append("§")
									.append(String.valueOf(x).toCharArray()[1]).toString()
							: new StringBuilder("§0§").append(x).toString())
					+ ((y > 9) ? ("§" + String.valueOf(y).toCharArray()[0] + "§" + String.valueOf(y).toCharArray()[1])
							: ("§0§" + String.valueOf(y).toCharArray()[0]));
			final UUID id = UUID.randomUUID();
			final Object profile = ReflectionConstants.GAME_PROFILE_CONSTRUCTOR.invoke(id, name);
			Tablist.TAB_NAMES[i] = name;
			Tablist.GAME_PROFILES[i] = profile;
		}
	}

	public Tablist(final Player player) {
		this.player = player;
		this.version = ClientVersion.getVersion(player);
		Scoreboard scoreboard = player.getScoreboard();
		if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
			scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		}
		player.setScoreboard(scoreboard);
		this.scoreboard = scoreboard;
		this.addFakePlayers();
		this.update();
	}

	public void sendPacket(final Player player, final Object packet) {
		final Object handle = ReflectionConstants.GET_HANDLE_METHOD.invoke(player, new Object[0]);
		final Object connection = ReflectionConstants.PLAYER_CONNECTION.get(handle);
		ReflectionConstants.SEND_PACKET.invoke(connection, packet);
	}

	public Tablist update() {
		final TablistManager manager = TablistManager.INSTANCE;
		if (!this.initiated || manager == null) {
			return this;
		}
		final Table<Integer, Integer, String> entries = manager.getSupplier().getEntries(this.player);
		final boolean useProfiles = this.version.ordinal() != 0;
		final int magic = useProfiles ? 4 : 3;
		if (useProfiles) {
			String header = manager.getSupplier().getHeader(this.player);
			if (header == null) {
				header = "";
			}
			String footer = manager.getSupplier().getFooter(this.player);
			if (footer == null) {
				footer = "";
			}
			final ProtocolInjector.PacketTabHeader packet = new ProtocolInjector.PacketTabHeader(
					ChatSerializer.a("{text:\"" + StringEscapeUtils.escapeJava(header) + "\"}"),
					ChatSerializer.a("{text:\"" + StringEscapeUtils.escapeJava(footer) + "\"}"));
			this.sendPacket(this.player, packet);
		}
		for (int i = 0; i < magic * 20; ++i) {
			final int x = i % magic;
			final int y = i / magic;
			String text = (String) entries.get((Object) x, (Object) y);
			if (text == null) {
				text = "";
			}
			final String name = Tablist.TAB_NAMES[i];
			Team team = this.scoreboard.getTeam(name);
			if (team == null) {
				team = this.scoreboard.registerNewTeam(name);
			}
			if (!team.hasEntry(name)) {
				team.addEntry(name);
			}
			String prefix = "";
			String suffix = "";
			if (text.length() < 17) {
				prefix = text;
			} else {
				String left = text.substring(0, 16);
				String right = text.substring(16, text.length());
				if (left.endsWith("§")) {
					left = left.substring(0, left.length() - 1);
					right = "§" + right;
				}
				final String last = ChatColor.getLastColors(left);
				right = String.valueOf(last) + right;
				prefix = left;
				suffix = StringUtils.left(right, 16);
			}
			team.setPrefix(prefix);
			team.setSuffix(suffix);
		}
		return this;
	}

	public Tablist hideRealPlayers() {
		if (!this.initiated) {
			return this;
		}
		final boolean useProfiles = this.version.ordinal() != 0;
		Player[] onlinePlayers;
		for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i) {
			final Player online = onlinePlayers[i];
			if (!this.player.canSee(online)) {
				return this;
			}
			final Object packet = ReflectionConstants.TAB_PACKET_CONSTRUCTOR.invoke(new Object[0]);
			if (useProfiles) {
				final Object profile = ReflectionConstants.GET_PROFILE_METHOD.invoke(online, new Object[0]);
				ReflectionConstants.TAB_PACKET_PROFILE.set(packet, profile);
			} else {
				ReflectionConstants.TAB_PACKET_NAME.set(packet, online.getName());
			}
			ReflectionConstants.TAB_PACKET_ACTION.set(packet, 4);
			this.sendPacket(this.player, packet);
		}
		return this;
	}

	public Tablist hideFakePlayers() {
		//
		// This method could not be decompiled.
		//
		// Original Bytecode:
		//
		// 1: getfield us/monkeyhcf/kitmap/tablist/Tablist.initiated:Z
		// 4: ifne 9
		// 7: aload_0 /* this */
		// 8: areturn
		// 9: aload_0 /* this */
		// 10: getfield
		// us/monkeyhcf/kitmap/tablist/Tablist.version:Lus/monkeyhcf/kitmap/tablist/ClientVersion;
		// 13: invokevirtual us/monkeyhcf/kitmap/tablist/ClientVersion.ordinal:()I
		// 16: ifeq 23
		// 19: iconst_1
		// 20: goto 24
		// 23: iconst_0
		// 24: istore_1 /* useProfiles */
		// 25: getstatic
		// us/monkeyhcf/kitmap/tablist/Tablist.GAME_PROFILES:[Ljava/lang/Object;
		// 28: invokestatic
		// java/util/Arrays.stream:([Ljava/lang/Object;)Ljava/util/stream/Stream;
		// 31: aload_0 /* this */
		// 32: iload_1 /* useProfiles */
		// 33: invokedynamic BootstrapMethod #0,
		// accept:(Lus/monkeyhcf/kitmap/tablist/Tablist;Z)Ljava/util/function/Consumer;
		// 38: invokeinterface
		// java/util/stream/Stream.forEach:(Ljava/util/function/Consumer;)V
		// 43: aload_0 /* this */
		// 44: areturn
		// StackMapTable: 00 03 09 0D 40 01
		//
		// The error that occurred was:
		//
		// java.lang.NullPointerException
		// at
		// com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
		// at
		// com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
		// at
		// com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
		// at
		// com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
		// at
		// com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
		// at
		// com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
		// at
		// com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
		// at
		// com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
		// at
		// com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
		// at
		// com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
		// at
		// com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
		// at
		// com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
		// at
		// com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
		// at
		// us.deathmarine.luyten.DecompilerLinkProvider.generateContent(DecompilerLinkProvider.java:97)
		// at
		// us.deathmarine.luyten.OpenFile.decompileWithNavigationLinks(OpenFile.java:494)
		// at us.deathmarine.luyten.OpenFile.decompile(OpenFile.java:467)
		// at us.deathmarine.luyten.Model.extractClassToTextPane(Model.java:420)
		// at us.deathmarine.luyten.Model.openEntryByTreePath(Model.java:339)
		// at us.deathmarine.luyten.Model$TreeListener$1.run(Model.java:266)
		//
		throw new IllegalStateException("An error occurred while decompiling this method.");
	}

	public Tablist addFakePlayers() {
		if (this.initiated) {
			return this;
		}
		final boolean useProfiles = this.version.ordinal() != 0;
		for (int magic = useProfiles ? 4 : 3, i = 0; i < magic * 20; ++i) {
			final int x = i % magic;
			final int y = i / magic;
			final Object packet = ReflectionConstants.TAB_PACKET_CONSTRUCTOR.invoke(new Object[0]);
			final Object profile = Tablist.GAME_PROFILES[i];
			if (useProfiles) {
				ReflectionConstants.TAB_PACKET_PROFILE.set(packet, profile);
			} else {
				final String name = ReflectionConstants.GAME_PROFILE_NAME.get(profile);
				ReflectionConstants.TAB_PACKET_NAME.set(packet, name);
			}
			ReflectionConstants.TAB_PACKET_ACTION.set(packet, 0);
			this.sendPacket(this.player, packet);
		}
		this.initiated = true;
		return this;
	}

	public void clear() {
		this.player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		this.scoreboard.getTeams().forEach(team -> team.unregister());
		this.scoreboard = null;
		System.gc();
	}

	public ClientVersion getVersion() {
		return this.version;
	}

	public Player getPlayer() {
		return this.player;
	}

	public boolean isInitiated() {
		return this.initiated;
	}
}
