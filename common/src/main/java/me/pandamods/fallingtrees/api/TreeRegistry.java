package me.pandamods.fallingtrees.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class TreeRegistry {
	private static final Map<ResourceLocation, Tree> REGISTRIES = new HashMap<>();

	public static Supplier<Tree> register(ResourceLocation resourceLocation, Supplier<Tree> treeSupplier) {
		REGISTRIES.put(resourceLocation, treeSupplier.get());
		return treeSupplier;
	}

	public static Optional<Tree> getTree(BlockState blockState) {
		return REGISTRIES.values().stream().filter(treeType -> treeType.enabled() && treeType.mineableBlock(blockState)).findFirst();
	}

	public static Optional<Tree> getTree(ResourceLocation resourceLocation) {
		return Optional.ofNullable(REGISTRIES.get(resourceLocation));
	}

	public static ResourceLocation getTreeLocation(Tree tree) {
		for (Map.Entry<ResourceLocation, Tree> entry : REGISTRIES.entrySet()) {
			if (entry.getValue() == tree) return entry.getKey();
		}
		return null;
	}
}
