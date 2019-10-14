package com.zdev.hcf.faction.argument;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

import com.zdev.hcf.Base;
import com.zdev.hcf.faction.struct.Role;
import com.zdev.hcf.faction.type.PlayerFaction;
import com.zdev.hcf.util.Color;
import com.zdev.hcf.util.command.CommandArgument;

import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;

public class FactionManagerArgument extends CommandArgument {
	private final Base plugin;
	public Inventory factionManager;

	public FactionManagerArgument(final Base plugin) {
		super("manage", "Manage your faction using a GUI");
		this.plugin = plugin;
	}

	@Override
	public String getUsage(final String label) {
		return '/' + label;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public boolean onCommand(CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		Player player = (Player) sender;
		PlayerFaction playerFaction = Base.getInstance().getFactionManager().getPlayerFaction(player);
		this.factionManager = Bukkit.createInventory((InventoryHolder) null, 36, "Faction Managment");
		if (playerFaction == null) {
			player.sendMessage(ChatColor.RED + "You don't have a faction");
			return true;
		}
		if (playerFaction.getMember(player).getRole() == Role.LEADER) {
			player.openInventory(this.factionManager);
			for (final Player p : playerFaction.getOnlinePlayers()) {
				final ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
				final ItemMeta meta = skull.getItemMeta();
				meta.setLore(
						(List) Arrays.asList(Color.translate("&eDo &6&lRIGHT CLICK &eto &6&lDEMOTE &ethis player."),
								Color.translate("&eDo &6&lLEFT CLICK &eto &6&lPROMOTE&e this player."),
								Color.translate("&eDo &6&lMIDDLE CLICK &eto make &6&lLEADER&e this player.")));
				meta.setDisplayName(p.getName());
				skull.setItemMeta(meta);
				this.factionManager.addItem(new ItemStack[] { skull });
			}
		} else {
			player.sendMessage(ChatColor.RED + "You are not a leader.");
		}
		return false;
	}

	@EventHandler
	public void onInventoryClick(final InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();
		final ItemStack clicked = event.getCurrentItem();
		final Inventory inventory = event.getInventory();
		if (inventory.getName().equals("Faction Managment") && clicked.getType() == Material.SKULL_ITEM) {
			if (event.getClick() == ClickType.LEFT) {
				Bukkit.dispatchCommand((CommandSender) player, "f promote " + clicked.getItemMeta().getDisplayName());
				event.setCancelled(true);
			}
			if (event.getClick() == ClickType.MIDDLE) {
				Bukkit.dispatchCommand((CommandSender) player, "f leader " + clicked.getItemMeta().getDisplayName());
				event.setCancelled(true);
			}
			if (event.getClick() == ClickType.RIGHT) {
				Bukkit.dispatchCommand((CommandSender) player, "f demote " + clicked.getItemMeta().getDisplayName());
				event.setCancelled(true);
			}
		}
	}
}
