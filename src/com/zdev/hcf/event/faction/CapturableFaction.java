package com.zdev.hcf.event.faction;

import java.util.Map;

public abstract class CapturableFaction extends EventFaction {
	public CapturableFaction(String name) {
		super(name);
	}

	public CapturableFaction(Map<String, Object> map) {
		super(map);
	}
}
