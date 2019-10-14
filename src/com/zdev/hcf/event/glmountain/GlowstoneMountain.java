package com.zdev.hcf.event.glmountain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.internal.annotation.Selection;
import com.zdev.hcf.Base;
import com.zdev.hcf.serializable.SerializableLocation;

public final class GlowstoneMountain implements CommandExecutor {

	private final Base plugin;

	public GlowstoneMountain(Base plugin) {
		this.plugin = plugin;
		new GlowstoneRunnable().runTaskTimer(plugin, 0L, 20 * 60 * 15);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("command.glowstone")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
			return true;
		}

		if (args.length == 0 || args.length > 1) {
			sender.sendMessage("§9§m--------------------------------");
			sender.sendMessage(
					"§c/glowstone set - Sets the location for Glowstone Mountain. §7(Make sure you have made a selection with WorldEdit)");
			sender.sendMessage("§c/glowstone reset - Resets Glowstone Mountain.");
			sender.sendMessage("§9§m--------------------------------");
			return true;
		}

		if (args[0].equalsIgnoreCase("reset"))
			this.resetGlowstoneMountain();
		else if (args[0].equalsIgnoreCase("set")) {
			Player player = (Player) sender;
			Selection selection = (Selection) this.plugin.getWorldEdit().getSelection(player);

			if (selection == null) {
				player.sendMessage(ChatColor.RED + "You need to make a selection first!");
				return true;
			}

			SerializableLocation min = new SerializableLocation(
					((com.sk89q.worldedit.bukkit.selections.Selection) selection).getMinimumPoint()),
					max = new SerializableLocation(
							((com.sk89q.worldedit.bukkit.selections.Selection) selection).getMaximumPoint());

			plugin.getConfig().set("Location.glowstone.min", min);
			plugin.getConfig().set("Location.glowstone.max", max);
			plugin.saveConfig();

			player.sendMessage(ChatColor.GREEN + "Set glowstone mountain location!");
		}

		return false;
	}

	public void resetGlowstoneMountain() {
		if (plugin.getConfig().get("Location.glowstone.min") == null
				|| plugin.getConfig().get("Location.glowstone.max") == null)
			return;

		Location min = ((SerializableLocation) plugin.getConfig().get("Location.glowstone.min")).getLocation();
		Location max = ((SerializableLocation) plugin.getConfig().get("Location.glowstone.max")).getLocation();

		if (min == null || max == null)
			return;

		int minY = min.getBlockY(), maxY = max.getBlockY();
		Block block = null;

		for (; minY <= maxY; minY++) {
			for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
					block = min.getWorld().getBlockAt(x, minY, z);

					if (!block.getChunk().isLoaded()) {
						block.getChunk().load();
						continue;
					}

					block.setType(Material.GLOWSTONE);
				}
			}
		}
		Bukkit.broadcastMessage(
				ChatColor.translateAlternateColorCodes('&', "&aGlowstone Mountain has been &a&lreset&a!"));
	}

	public class GlowstoneRunnable extends BukkitRunnable {

		@Override
		public void run() {
			GlowstoneMountain.this.resetGlowstoneMountain();
		}

	}
}
