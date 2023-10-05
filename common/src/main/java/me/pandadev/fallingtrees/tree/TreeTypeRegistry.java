package me.pandadev.fallingtrees.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TreeTypeRegistry {
	private static final Map<ResourceLocation, TreeType> TYPES = new HashMap<>();

	public static void register(ResourceLocation name, TreeType treeType) {
		TYPES.put(name, treeType);
	}

	public static Optional<TreeType> getTreeType(BlockPos pos, BlockGetter level) {
		return TYPES.values().stream().filter(treeType -> treeType.blockChecker(pos, level)).findFirst();
	}

	public static ResourceLocation getTreeTypeName(TreeType type) {
		Optional<Map.Entry<ResourceLocation, TreeType>> name = TYPES.entrySet().stream().filter(entry -> entry.getValue() == type).findFirst();
		return name.map(Map.Entry::getKey).orElse(null);
	}

	public static Optional<TreeType> getTreeType(ResourceLocation name) {
		return Optional.ofNullable(TYPES.get(name));
	}
}
