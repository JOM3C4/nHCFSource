package com.zdev.hcf.faction.type;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;

public class GlowstoneFaction extends ClaimableFaction implements ConfigurationSerializable {
	public GlowstoneFaction() {
		super("Glowstone");
		this.safezone = false;
	}

	public GlowstoneFaction(Map<String, Object> map) {
		super(map);
	}

	@Override
	public String getDisplayName(CommandSender sender) {
		return ChatColor.GOLD + this.getName().replace("Glowstone Mountain", "Glowstone Mountain");
	}

	@Override
	public boolean isDeathban() {
		return true;
	}
}
