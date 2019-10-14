package com.zdev.hcf.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.zdev.hcf.Base;
import com.zdev.hcf.listener.DeathListener;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

public class RefundCommand implements CommandExecutor, Listener {
	private Base mainPlugin;
	private Map<UUID, InventorySet> rollbackInv;

	public Map<UUID, InventorySet> getRollbackInv() {
		return this.rollbackInv;
	}

	public RefundCommand(Base mainPlugin) {
		this.mainPlugin = mainPlugin;
		this.mainPlugin.getServer().getPluginManager().registerEvents(this, this.mainPlugin);

		this.rollbackInv = new HashMap<UUID, InventorySet>();
	}

	public class InventorySet {
		private Player p;
		private ItemStack[] inv;
		private ItemStack[] armor;

		public Player getP() {
			return this.p;
		}

		public ItemStack[] getInv() {
			return this.inv;
		}

		public ItemStack[] getArmor() {
			return this.armor;
		}

		public InventorySet(Player p) {
			this.p = p;

			this.inv = p.getInventory().getContents();
			this.armor = p.getInventory().getArmorContents();
		}
	}

	public boolean onCommand(CommandSender s, Command c, String alias, String[] args) {
		if (!s.hasPermission("core.refund")) {
			s.sendMessage(ChatColor.RED + "You do not have permission.");
			return true;
		}
		if (args.length != 1) {
			s.sendMessage(ChatColor.RED + "Correct Usage: /" + c.getName() + " <player>");
			return true;
		}
		Player p = this.mainPlugin.getServer().getPlayer(args[0]);
		if (p == null) {
			s.sendMessage(ChatColor.RED + "Player is not online.");
			return true;
		}
		if (!this.rollbackInv.containsKey(p.getUniqueId())) {
			s.sendMessage(ChatColor.RED + p.getName() + " does not have a stored record of his/her inventory.");
		} else {
			s.sendMessage(ChatColor.GREEN + p.getName() + "'s inventory has been restored.");
			p.sendMessage(ChatColor.GREEN + "Your inventory was restored by " + s.getName() + ".");

			InventorySet invSet = (InventorySet) this.rollbackInv.get(p.getUniqueId());

			p.getInventory().setContents(invSet.getInv());
			p.getInventory().setArmorContents(invSet.getArmor());
		}
		return true;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDeath(PlayerDeathEvent e) {
		UUID entityUUID = e.getEntity().getUniqueId();
		this.rollbackInv.put(entityUUID, new InventorySet(e.getEntity()));
	}
}