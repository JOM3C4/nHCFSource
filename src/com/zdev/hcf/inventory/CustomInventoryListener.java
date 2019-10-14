package com.zdev.hcf.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface CustomInventoryListener {

	public void onClick(InventoryClickEvent event);

	public void onDrag(InventoryDragEvent event);

	public void onOpen(InventoryOpenEvent event);

	public void onClose(InventoryCloseEvent event);

}
