package me.pandadev.fallingtrees.registries;

import me.pandadev.fallingtrees.tree.TreeTypeRegistry;
import me.pandadev.fallingtrees.trees.BambooTreeType;
import me.pandadev.fallingtrees.trees.CactusTreeType;
import me.pandadev.fallingtrees.trees.DefaultTreeType;

public class TreeTypes {
	public static void init() {
		TreeTypeRegistry.register(new DefaultTreeType());
		TreeTypeRegistry.register(new CactusTreeType());
		TreeTypeRegistry.register(new BambooTreeType());
	}
}