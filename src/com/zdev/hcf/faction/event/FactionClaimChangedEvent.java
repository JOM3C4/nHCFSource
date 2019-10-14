package com.zdev.hcf.faction.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.zdev.hcf.faction.claim.Claim;
import com.zdev.hcf.faction.event.cause.ClaimChangeCause;

import java.util.Collection;

public class FactionClaimChangedEvent extends Event {
	private static final HandlerList handlers;

	static {
		handlers = new HandlerList();
	}

	private final CommandSender sender;
	private final ClaimChangeCause cause;
	private final Collection<Claim> affectedClaims;

	public FactionClaimChangedEvent(final CommandSender sender, final ClaimChangeCause cause,
			final Collection<Claim> affectedClaims) {
		this.sender = sender;
		this.cause = cause;
		this.affectedClaims = affectedClaims;
	}

	public static HandlerList getHandlerList() {
		return FactionClaimChangedEvent.handlers;
	}

	public CommandSender getSender() {
		return this.sender;
	}

	public ClaimChangeCause getCause() {
		return this.cause;
	}

	public Collection<Claim> getAffectedClaims() {
		return this.affectedClaims;
	}

	public HandlerList getHandlers() {
		return FactionClaimChangedEvent.handlers;
	}
}
