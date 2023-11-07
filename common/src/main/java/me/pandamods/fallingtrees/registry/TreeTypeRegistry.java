package me.pandamods.fallingtrees.registry;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.api.TreeRegistry;
import me.pandamods.fallingtrees.api.TreeType;
import me.pandamods.fallingtrees.trees.DefaultTree;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class TreeTypeRegistry {

	public static final Supplier<TreeType> DEFAULT = TreeRegistry.register(new ResourceLocation(FallingTrees.MOD_ID, "default"), DefaultTree::new);

	public static void register() {
	}
}
