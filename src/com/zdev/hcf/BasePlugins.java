package com.zdev.hcf;

import java.util.Random;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import com.zdev.hcf.serializable.SerializableLocation;
import com.zdev.hcf.util.PersistableLocation;
import com.zdev.hcf.util.SignHandler;
import com.zdev.hcf.util.cuboid.Cuboid;
import com.zdev.hcf.util.cuboid.NamedCuboid;
import com.zdev.hcf.util.itemdb.ItemDb;
import com.zdev.hcf.util.itemdb.SimpleItemDb;

public class BasePlugins {
	private Random random;
	private ItemDb itemDb;
	private SignHandler signHandler;
	private static BasePlugins plugin;
	private JavaPlugin javaPlugin;

	private BasePlugins() {
		this.random = new Random();
	}

	public void init(final JavaPlugin plugin) {
		this.javaPlugin = plugin;
		ConfigurationSerialization.registerClass(SerializableLocation.class);
		ConfigurationSerialization.registerClass(PersistableLocation.class);
		ConfigurationSerialization.registerClass(Cuboid.class);
		ConfigurationSerialization.registerClass(NamedCuboid.class);
		this.itemDb = new SimpleItemDb(plugin);
		this.signHandler = new SignHandler(plugin);
	}

	public void disable() {
		this.signHandler.cancelTasks(null);
		this.javaPlugin = null;
		BasePlugins.plugin = null;
	}

	public Random getRandom() {
		return this.random;
	}

	public ItemDb getItemDb() {
		return this.itemDb;
	}

	public SignHandler getSignHandler() {
		return this.signHandler;
	}

	public static BasePlugins getPlugin() {
		return BasePlugins.plugin;
	}

	public JavaPlugin getJavaPlugin() {
		return this.javaPlugin;
	}

	static {
		BasePlugins.plugin = new BasePlugins();
	}
}
