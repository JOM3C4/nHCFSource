package com.zdev.hcf.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.zdev.hcf.Base;
import com.zdev.hcf.ConfigurationService;

public class KitMapListener implements Listener {

	final Base plugin;

	public KitMapListener(Base plugin) {
		this.plugin = plugin;
	}

//    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
//    public void onFactionClaimChange(FactionClaimChangeEvent event){
//        if(event.getReason() == ClaimChangeEvent.ClaimChangeReason.CLAIM && ConfigurationService.KIT_MAP && event.getFaction() instanceof PlayerFaction){
//            event.setCancelled(true);
//            event.getSender().sendMessage(ChatColor.RED + "You cannot claim land because this map is a kit map.");
//        }
//    }

	// TODO: Memory leak

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (ConfigurationService.KIT_MAP) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (ConfigurationService.KIT_MAP
				&& plugin.getFactionManager().getFactionAt(event.getItemDrop().getLocation()).isSafezone()) {
			event.getItemDrop().remove();
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onItemSpawn(ItemSpawnEvent event) {
		if (ConfigurationService.KIT_MAP && plugin.getFactionManager().getFactionAt(event.getLocation()).isSafezone()) {
			event.getEntity().remove();
		}
	}
}
