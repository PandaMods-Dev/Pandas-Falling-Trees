package me.pandamods.fallingtrees.registry;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.api.TreeRegistry;
import me.pandamods.fallingtrees.api.Tree;
import me.pandamods.fallingtrees.trees.BambooTree;
import me.pandamods.fallingtrees.trees.CactusTree;
import me.pandamods.fallingtrees.trees.ChorusTree;
import me.pandamods.fallingtrees.trees.StandardTree;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class TreeTypeRegistry {

	public static final Supplier<StandardTree> DEFAULT = TreeRegistry.register(new ResourceLocation(FallingTrees.MOD_ID, "default"), StandardTree::new);
	public static final Supplier<CactusTree> CACTUS = TreeRegistry.register(new ResourceLocation(FallingTrees.MOD_ID, "cactus"), CactusTree::new);
	public static final Supplier<BambooTree> BAMBOO = TreeRegistry.register(new ResourceLocation(FallingTrees.MOD_ID, "bamboo"), BambooTree::new);
	public static final Supplier<ChorusTree> CHORUS = TreeRegistry.register(new ResourceLocation(FallingTrees.MOD_ID, "chorus"), ChorusTree::new);
	
	public static void register() {
	}
}
