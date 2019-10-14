package com.zdev.hcf.event.conquest;

import com.zdev.hcf.Base;
import com.zdev.hcf.util.command.ArgumentExecutor;

public class ConquestExecutor extends ArgumentExecutor {
	public ConquestExecutor(Base plugin) {
		super("conquest");
		addArgument(new ConquestSetpointsArgument(plugin));
	}
}
