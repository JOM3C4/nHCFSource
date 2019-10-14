package com.zdev.hcf.commands.youtube;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zdev.hcf.util.BukkitUtils;
import com.zdev.hcf.util.CC;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class NewVideoCommand implements CommandExecutor {
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players!");
			return true;
		}
		final Player player = (Player) sender;

		if (args.length == 0) {
			player.sendMessage(ChatColor.RED + "Usage: /newvideo <url>");
			return true;
		}
		if (args.length == 1) {
			final String url = args[0];
			if (!url.toLowerCase().startsWith("https://") || !url.toLowerCase().contains("youtube.com")) {
				player.sendMessage(CC.translate("&cInvalid url!"));
				player.sendMessage(
						CC.translate("&cExample: /newvideo &ehttps://www.youtube.com/c/" + player.getName()));
				return true;
			}
			Bukkit.broadcastMessage(ChatColor.BOLD + BukkitUtils.STRAIGHT_LINE_DEFAULT);
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(
					CC.translate("&f&lYou&4&lTube &8» " + player.getDisplayName() + " &7has published a new video."));
			Bukkit.spigot().broadcast(this.newVideo(player, url));
			Bukkit.broadcastMessage(CC.translate(" "));
			Bukkit.broadcastMessage(ChatColor.BOLD + BukkitUtils.STRAIGHT_LINE_DEFAULT);
			player.sendMessage("§4§lALERT: §cSpamming with this command, may result a PUNISHMENT.");
		}
		return false;
	}

	private BaseComponent newVideo(final Player player, final String url) {
		final String msg = String.valueOf(CC.translate("   &f&l* &a&lLINK"));
		final TextComponent text = new TextComponent(msg);
		text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(CC.translate("&7Click to copy!")).create()));
		return (BaseComponent) text;
	}
}
