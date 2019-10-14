package com.zdev.hcf;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.zdev.hcf.util.chat.Text;

public final class Message {
	Base plugin;
	private final HashMap<UUID, Long> messageDelay;

	public Message(Base plugin) {
		this.messageDelay = new HashMap<UUID, Long>();
		this.plugin = plugin;
	}

	public void sendMessage(Player player, String message) {
		if (this.messageDelay.containsKey(player.getUniqueId())) {
			if (((Long) this.messageDelay.get(player.getUniqueId())).longValue() - System.currentTimeMillis() > 0L) {
				return;
			}
			this.messageDelay.remove(player.getUniqueId());
		}
		this.messageDelay.putIfAbsent(player.getUniqueId(), Long.valueOf(System.currentTimeMillis() + 3000L));
		player.sendMessage(message);
	}

	public void sendMessage(Player player, Text text) {
		if (this.messageDelay.containsKey(player.getUniqueId())) {
			if (((Long) this.messageDelay.get(player.getUniqueId())).longValue() - System.currentTimeMillis() > 0L) {
				return;
			}
			this.messageDelay.remove(player.getUniqueId());
		}
		this.messageDelay.putIfAbsent(player.getUniqueId(), Long.valueOf(System.currentTimeMillis() + 3000L));
		text.send(player);
	}
}
