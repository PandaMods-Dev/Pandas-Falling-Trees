package me.pandadev.fallingtrees.registries;

import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.tree.TreeTypeRegistry;
import me.pandadev.fallingtrees.trees.BambooTreeType;
import me.pandadev.fallingtrees.trees.CactusTreeType;
import me.pandadev.fallingtrees.trees.DefaultTreeType;
import net.minecraft.resources.ResourceLocation;

public class TreeTypes {
	public static void init() {
		TreeTypeRegistry.register(new ResourceLocation(FallingTrees.MOD_ID, "standard"), new DefaultTreeType());
		TreeTypeRegistry.register(new ResourceLocation(FallingTrees.MOD_ID, "cactus"), new CactusTreeType());
		TreeTypeRegistry.register(new ResourceLocation(FallingTrees.MOD_ID, "bamboo"), new BambooTreeType());
	}
}