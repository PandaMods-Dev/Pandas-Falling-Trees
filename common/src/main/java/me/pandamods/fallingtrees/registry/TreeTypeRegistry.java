package me.pandamods.fallingtrees.registry;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.api.TreeRegistry;
import me.pandamods.fallingtrees.trees.DefaultTree;
import net.minecraft.resources.ResourceLocation;

public class TreeTypeRegistry {
	public static void register() {
		TreeRegistry.register(new ResourceLocation(FallingTrees.MOD_ID, "default"), DefaultTree::new);
	}
}
