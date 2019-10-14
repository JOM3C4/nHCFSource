package com.zdev.hcf.timer.type;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.zdev.hcf.Base;
import com.zdev.hcf.classes.archer.ArcherClass;
import com.zdev.hcf.timer.PlayerTimer;
import com.zdev.hcf.timer.event.TimerExpireEvent;

public class ArcherTimer extends PlayerTimer implements Listener {
	private final Base plugin;

	public String getScoreboardPrefix() {
		return ChatColor.GOLD.toString() + ChatColor.BOLD;
	}

	public ArcherTimer(Base plugin) {
		super("Archer Tag", TimeUnit.SECONDS.toMillis(10L));
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onExpire(TimerExpireEvent e) {
		if ((e.getUserUUID().isPresent()) && (e.getTimer().equals(this))) {
			UUID userUUID = (UUID) e.getUserUUID().get();
			Player player = Bukkit.getPlayer(userUUID);
			if (player == null) {
				return;
			}
			ArcherClass.TAGGED.remove(player.getUniqueId());
			List<Player> onlinePlayers = new ArrayList<Player>();
			Player[] arrayOfPlayer;
			int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
			for (int i = 0; i < j; i++) {
				Player players = arrayOfPlayer[i];
				onlinePlayers.add(players);
				this.plugin.getScoreboardHandler().getPlayerBoard(players.getUniqueId()).addUpdates(onlinePlayers);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof Player))) {
			Player entity = (Player) e.getEntity();
			e.getDamager();
			if (getRemaining(entity) > 0L) {
				Double damage = Double.valueOf(e.getDamage() * 0.25D);
				e.setDamage(e.getDamage() + damage.doubleValue());
			}
		}
		if (((e.getEntity() instanceof Player)) && ((e.getDamager() instanceof Arrow))) {
			Player entity = (Player) e.getEntity();
			Entity damager = ((Arrow) e.getDamager()).getShooter();
			if (((damager instanceof Player)) && (getRemaining(entity) > 0L)) {
				if (((UUID) ArcherClass.TAGGED.get(entity.getUniqueId())).equals(damager.getUniqueId())) {
					setCooldown(entity, entity.getUniqueId());
				}
				Double damage = Double.valueOf(e.getDamage() * 0.25D);
				e.setDamage(e.getDamage() + damage.doubleValue());
			}
		}
	}
}
