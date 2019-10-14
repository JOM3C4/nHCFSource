package com.zdev.hcf.tablist;

import org.bukkit.entity.*;

import com.zdev.hcf.tablist.reflection.ReflectionConstants;

public enum ClientVersion {
	v1_7, v1_7_10, v1_8;

	public static ClientVersion getVersion(final Player player) {
		final Object handle = ReflectionConstants.GET_HANDLE_METHOD.invoke(player, new Object[0]);
		final Object connection = ReflectionConstants.PLAYER_CONNECTION.get(handle);
		final Object manager = ReflectionConstants.NETWORK_MANAGER.get(connection);
		final Object version = ReflectionConstants.VERSION_METHOD.invoke(manager, new Object[0]);
		if (version instanceof Integer) {
			return ((int) version > 5) ? ClientVersion.v1_8 : ClientVersion.v1_7;
		}
		return ClientVersion.v1_7;
	}
}
