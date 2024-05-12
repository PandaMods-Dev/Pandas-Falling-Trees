package me.pandamods.fallingtrees.registry;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.api.TreeRegistry;
import me.pandamods.fallingtrees.trees.MushroomTree;
import me.pandamods.fallingtrees.trees.VerticalTree;
import me.pandamods.fallingtrees.trees.ChorusTree;
import me.pandamods.fallingtrees.trees.StandardTree;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class TreeTypeRegistry {

	public static final Supplier<StandardTree> DEFAULT = TreeRegistry.register(new ResourceLocation(FallingTrees.MOD_ID, "default"), StandardTree::new);
	public static final Supplier<VerticalTree> VERTICAL = TreeRegistry.register(new ResourceLocation(FallingTrees.MOD_ID, "vertical"), VerticalTree::new);
	public static final Supplier<ChorusTree> CHORUS = TreeRegistry.register(new ResourceLocation(FallingTrees.MOD_ID, "chorus"), ChorusTree::new);
	public static final Supplier<MushroomTree> MUSHROOM = TreeRegistry.register(new ResourceLocation(FallingTrees.MOD_ID, "mushroom"), MushroomTree::new);

	public static void register() {
	}
}
