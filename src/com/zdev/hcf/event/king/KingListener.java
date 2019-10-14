package com.zdev.hcf.event.king;

import org.bukkit.scheduler.BukkitRunnable;

import com.zdev.hcf.Base;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;

public class KingListener implements Listener {
	private RebootRunnable rebootRunnable;
	Base plugin;

	public boolean cancel() {
		if (this.rebootRunnable != null) {
			this.rebootRunnable.cancel();
			this.rebootRunnable = null;
			return true;
		}
		return false;
	}

	public void start(final long millis) {
		if (this.rebootRunnable == null) {
			(this.rebootRunnable = new RebootRunnable(this, millis)).runTaskLater((Plugin) Base.getPlugin(),
					millis / 50L);
		}
	}

	public RebootRunnable getRebootRunnable() {
		return this.rebootRunnable;
	}

	public long getRemaining() {
		return RebootRunnable.endMillis - System.currentTimeMillis();
	}

	@EventHandler
	public void onDeath(final PlayerDeathEvent e) {
		final Player player = e.getEntity();
		if (KingCommand.player != null && player.getName().equals(KingCommand.kingName)) {
			Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "kingevent end");
			Bukkit.broadcastMessage(String.valueOf(String.valueOf(ChatColor.DARK_GREEN.toString())) + ChatColor.BOLD
					+ "THE KING HAS DIED");
			Bukkit.broadcastMessage(String.valueOf(String.valueOf(ChatColor.DARK_GREEN.toString())) + ChatColor.BOLD
					+ "THE KING HAS DIED");
			Bukkit.broadcastMessage(String.valueOf(String.valueOf(ChatColor.DARK_GREEN.toString())) + ChatColor.BOLD
					+ "THE KING HAS DIED");
		}
	}

	static void access$0(final KingListener kingListener, final RebootRunnable rebootRunnable) {
		kingListener.rebootRunnable = rebootRunnable;
	}

	public static class RebootRunnable extends BukkitRunnable {
		private KingListener rebootTimer;
		private long startMillis;
		private static long endMillis;

		public RebootRunnable(final KingListener rebootTimer, final long duration) {
			this.rebootTimer = rebootTimer;
			this.startMillis = System.currentTimeMillis();
			RebootRunnable.endMillis = this.startMillis + duration;
		}

		public long getRemaining() {
			return RebootRunnable.endMillis - System.currentTimeMillis();
		}

		public void run() {
			this.cancel();
			KingListener.access$0(this.rebootTimer, null);
		}
	}
}
