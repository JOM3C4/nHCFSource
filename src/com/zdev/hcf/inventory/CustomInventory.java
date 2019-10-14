package com.zdev.hcf.inventory;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;

public abstract class CustomInventory<P extends JavaPlugin> implements Listener, InventoryHolder {

	protected Inventory inventory;

	protected final Set<CustomInventoryListener> listeners;

	protected final P plugin;

	protected String name;

	protected final int size;

	public CustomInventory(P plugin, String name, int size) {
		this.inventory = Bukkit.createInventory(this, size, name);
		this.listeners = new HashSet<>();
		this.plugin = plugin;
		this.name = name;
		this.size = size;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		registerListeners();
	}

	public abstract void initialize();

	public abstract void registerListeners();

	public final void open(Player player) {
		initialize();
		player.openInventory(inventory);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onClick(InventoryClickEvent event) {
		if (event.getInventory().getHolder() == this && event.getWhoClicked() instanceof Player) {
			if (event.getAction() != InventoryAction.PICKUP_HALF) {
				listeners.forEach(listener -> {
					listener.onClick(event);
				});
			}
		}
	}

	@EventHandler
	public void onDrag(InventoryDragEvent event) {
		if (event.getInventory().getHolder() == this && event.getWhoClicked() instanceof Player) {
			listeners.forEach(listener -> {
				listener.onDrag(event);
			});
		}
	}

	@EventHandler
	public void onOpen(InventoryOpenEvent event) {
		if (event.getInventory().getHolder() == this && event.getPlayer() instanceof Player) {
			listeners.forEach(listener -> {
				listener.onOpen(event);
			});
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (event.getInventory().getHolder() == this && event.getPlayer() instanceof Player) {
			listeners.forEach(listener -> {
				listener.onClose(event);
			});
		}
	}

	// public void close() {
	// Collections.synchronizedSet(new
	// HashSet<>(inventory.getViewers())).forEach(HumanEntity::closeInventory);
	// HandlerList.unregisterAll(this);
	// }

}
