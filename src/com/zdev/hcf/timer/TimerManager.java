package com.zdev.hcf.timer;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.zdev.hcf.Base;
import com.zdev.hcf.doublekeys.DoubleKeyTimer;
import com.zdev.hcf.event.EventTimer;
import com.zdev.hcf.keyall.KeyAllTimer;
import com.zdev.hcf.keysale.KeySaleTimer;
import com.zdev.hcf.reboot.RebootTimer;
import com.zdev.hcf.sale.SaleTimer;
import com.zdev.hcf.timer.type.AppleTimer;
import com.zdev.hcf.timer.type.ArcherTimer;
import com.zdev.hcf.timer.type.EnderPearlTimer;
import com.zdev.hcf.timer.type.GappleTimer;
import com.zdev.hcf.timer.type.HomeTimer;
import com.zdev.hcf.timer.type.LogoutTimer;
import com.zdev.hcf.timer.type.PvPTimerProtection;
import com.zdev.hcf.timer.type.PvpClassWarmupTimer;
import com.zdev.hcf.timer.type.SpawnTagTimer;
import com.zdev.hcf.timer.type.StuckTimer;
import com.zdev.hcf.timer.type.TeleportTimer;
import com.zdev.hcf.tripplekeys.TrippleKeyTimer;
import com.zdev.hcf.util.Config;
import com.zdev.hcf.weekendsale.WeekendSaleTimer;

public class TimerManager implements Listener {

	private final RebootTimer rebootTimer;
	private final SaleTimer saleTimer;
	private final KeySaleTimer keysaleTimer;
	private final WeekendSaleTimer weekendsaleTimer;
	private final KeyAllTimer keyallTimer;
	private final TrippleKeyTimer tripplekeyTimer;
	private final DoubleKeyTimer doublekeyTimer;
	public final SpawnTagTimer combatTimer;
	public final LogoutTimer logoutTimer;
	public final EnderPearlTimer enderPearlTimer;
	public final EventTimer eventTimer;
	public final GappleTimer gappleTimer;
	public final PvPTimerProtection invincibilityTimer;
	private final PvpClassWarmupTimer pvpClassWarmupTimer;
	public final HomeTimer home;
	public final AppleTimer appleTimer;
	public final StuckTimer stuckTimer;

	public RebootTimer getRebootTimer() {
		return rebootTimer;
	}

	public SaleTimer getSaleTimer() {
		return saleTimer;
	}

	public KeyAllTimer getKeyAllTimer() {
		return keyallTimer;
	}

	public KeySaleTimer getKeySaleTimer() {
		return keysaleTimer;
	}

	public TrippleKeyTimer getTrippleKeyTimer() {
		return tripplekeyTimer;
	}

	public DoubleKeyTimer getDoubleKeyTimer() {
		return doublekeyTimer;
	}

	public WeekendSaleTimer getWeekendSaleTimer() {
		return weekendsaleTimer;
	}

	public final TeleportTimer teleportTimer;
	public final ArcherTimer archerTimer;
	private final Set<Timer> timers = new LinkedHashSet<>();
	private final JavaPlugin plugin;
	private Config config;
	public PvPTimerProtection pvpProtectionTimer;

	public TimerManager(Base plugin) {
		(this.plugin = plugin).getServer().getPluginManager().registerEvents(this, plugin);
		this.registerTimer(this.enderPearlTimer = new EnderPearlTimer(plugin));
		this.registerTimer(this.home = new HomeTimer());
		this.registerTimer(this.logoutTimer = new LogoutTimer());
		this.registerTimer(this.gappleTimer = new GappleTimer(plugin));
		this.registerTimer(this.stuckTimer = new StuckTimer());
		this.registerTimer(this.invincibilityTimer = new PvPTimerProtection(plugin));
		this.registerTimer(this.combatTimer = new SpawnTagTimer(plugin));
		this.registerTimer(this.teleportTimer = new TeleportTimer(plugin));
		this.registerTimer(this.eventTimer = new EventTimer(plugin));
		this.registerTimer(this.archerTimer = new ArcherTimer(plugin));
		this.registerTimer(this.pvpClassWarmupTimer = new PvpClassWarmupTimer(plugin));
		this.registerTimer(this.rebootTimer = new RebootTimer(plugin));
		this.registerTimer(this.saleTimer = new SaleTimer(plugin));
		this.registerTimer(this.keysaleTimer = new KeySaleTimer(plugin));
		this.registerTimer(this.tripplekeyTimer = new TrippleKeyTimer(plugin));
		this.registerTimer(this.doublekeyTimer = new DoubleKeyTimer(plugin));
		this.registerTimer(this.weekendsaleTimer = new WeekendSaleTimer(plugin));
		// this.registerTimer(this.doubleVoteTimer = new DoubleVoteTimer(plugin));
		this.registerTimer(this.keyallTimer = new KeyAllTimer(plugin));
		this.registerTimer(this.appleTimer = new AppleTimer(plugin));
		this.reloadTimerData();
	}

	public void registerTimer(Timer timer) {
		this.timers.add(timer);
		if (timer instanceof Listener) {
			this.plugin.getServer().getPluginManager().registerEvents((Listener) timer, this.plugin);
		}
	}

	public void unregisterTimer(Timer timer) {
		this.timers.remove(timer);
	}

	/**
	 * Reloads the {@link Timer} data from storage.
	 */
	public void reloadTimerData() {
		this.config = new Config(plugin, "timers");
		for (Timer timer : this.timers) {
			timer.load(this.config);
		}
	}

	/**
	 * Saves the {@link Timer} data to storage.
	 */
	public void saveTimerData() {
		for (Timer timer : this.timers) {
			timer.onDisable(this.config);
		}

		this.config.save();
	}

	public EventTimer getEventTimer() {
		return eventTimer;
	}

	public PvPTimerProtection getInvincibilityTimer() {
		return invincibilityTimer;
	}

	public SpawnTagTimer getCombatTimer() {
		return combatTimer;
	}

	public ArcherTimer getArcherTimer() {
		return archerTimer;
	}

	public EnderPearlTimer getEnderPearlTimer() {
		return enderPearlTimer;
	}

	public GappleTimer getGappleTimer() {
		return gappleTimer;
	}

	public PvpClassWarmupTimer getPvpClassWarmupTimer() {
		return pvpClassWarmupTimer;
	}

	public AppleTimer getAppleTimer() {
		return appleTimer;
	}

	public TeleportTimer getTeleportTimer() {
		return teleportTimer;
	}

	public StuckTimer getStuckTimer() {
		return stuckTimer;
	}

	public LogoutTimer getLogoutTimer() {
		return logoutTimer;
	}

	public Collection<Timer> getTimers() {
		return this.timers;

	}

}
