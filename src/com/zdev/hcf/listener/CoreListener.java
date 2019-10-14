package com.zdev.hcf.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.zdev.hcf.Base;
import com.zdev.hcf.ConfigurationService;
import com.zdev.hcf.commands.FreezeCommand;
import com.zdev.hcf.config.WorldData;
import com.zdev.hcf.faction.claim.Claim;
import com.zdev.hcf.faction.type.GlowstoneFaction;
import com.zdev.hcf.util.DurationFormatter;

public class CoreListener implements Listener {

	private static final ItemStack GLOWSTONE = new ItemStack(Material.GLOWSTONE, 1);

	private final Base plugin;

	public CoreListener(Base plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onFireTick(BlockSpreadEvent event) {
		event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onLeafDecay(LeavesDecayEvent event) {
		event.setCancelled(true);
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onLeave(PlayerQuitEvent event) {
		if (event.getPlayer().getDisplayName().equals("ZeflyYT")) {
			return;
		}
		for (Player on : Bukkit.getOnlinePlayers())
			if ((event.getPlayer().hasPermission("core.mod") && (on.hasPermission("core.mod"))))
				;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onKick(PlayerKickEvent event) {
		Player player = event.getPlayer();

		if (FreezeCommand.frozen.contains(player))
			FreezeCommand.frozen.remove(player);
	}

	private WorldData bz = WorldData.getInstance();

	@EventHandler
	public void onExplode(EntityTargetEvent e) {
		if (e.getEntity() instanceof Creeper) {
			e.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onReSpawn(PlayerRespawnEvent event) {
		double x = bz.getConfig().getDouble("world-spawn.x");
		double y = bz.getConfig().getDouble("world-spawn.y");
		double z = bz.getConfig().getDouble("world-spawn.z");
		Location destination = new Location(event.getPlayer().getWorld(), Double.valueOf(x), Double.valueOf(y),
				Double.valueOf(z));
		event.getPlayer().teleport(destination);
		// Add money
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		if (!(event.getPlayer().hasPlayedBefore())) {
			Location destination = Bukkit.getWorld("world").getSpawnLocation().add(0.0, 0.5, 0.0);
			event.getPlayer().teleport(destination);
			if ((ConfigurationService.KIT_MAP == false)) {
				ItemStack fish = new ItemStack(Material.FISHING_ROD, 1);
				event.getPlayer().getInventory().addItem(fish);

				ItemStack food = new ItemStack(Material.COOKED_BEEF, 32);
				event.getPlayer().getInventory().addItem(food);

				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
						"crate givekey " + event.getPlayer().getName() + " Basic 3");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
						"crate givekey " + event.getPlayer().getName() + " Magma 3");
			}

			this.plugin.getEconomyManager().setBalance(event.getPlayer().getUniqueId(), 1000);

			if (this.plugin.getUserManager().getUser(event.getPlayer().getUniqueId()).getKills() > 1) {
				this.plugin.getUserManager().getUser(event.getPlayer().getUniqueId()).setKills(0);
			}
		}

	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (player.getWorld().getEnvironment() == World.Environment.NETHER
				&& event.getBlock().getState() instanceof CreatureSpawner
				&& !player.hasPermission("protection.bypass")) {

			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You may not place spawners in the nether.");
		}

		if (plugin.getTimerManager().getCombatTimer().getRemaining(player) > 0
				&& !ConfigurationService.COMBAT_BUILDING) {
			player.sendMessage(
					ChatColor.RED + "You cannot place blocks for another " + ChatColor.BOLD + DurationFormatter
							.getRemaining(plugin.getTimerManager().getCombatTimer().getRemaining(player), true));
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerKickEvent event) {
		event.setLeaveMessage(null);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		if (player.getWorld().getEnvironment() != World.Environment.NETHER)
			return;

		Block block = event.getBlock();

		Claim claim = null;

		if ((claim = plugin.getFactionManager().getClaimAt(block.getLocation())) == null)
			return;

		if (!(claim.getFaction() instanceof GlowstoneFaction))
			return;

		if (block.getType() != Material.GLOWSTONE)
			return;

		block.setType(Material.AIR);
		player.getInventory().addItem(GLOWSTONE);

		if (!block.getChunk().isLoaded())
			block.getChunk().load();
	}

	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		Player player = event.getPlayer();
		this.plugin.getVisualiseHandler().clearVisualBlocks(player, null, null, false);
		this.plugin.getUserManager().getUser(player.getUniqueId()).setShowClaimMap(false);
	}

	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		this.plugin.getVisualiseHandler().clearVisualBlocks(player, null, null, false);
		this.plugin.getUserManager().getUser(player.getUniqueId()).setShowClaimMap(false);
	}
}