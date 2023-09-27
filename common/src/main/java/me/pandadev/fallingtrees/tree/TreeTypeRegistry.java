package me.pandadev.fallingtrees.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TreeTypeRegistry {
	private static final List<TreeType> TYPES = new ArrayList<>();

	public static void register(TreeType treeType) {
		TYPES.add(treeType);
	}

	public static Optional<TreeType> getTreeType(BlockPos pos, BlockGetter level) {
		return TYPES.stream().filter(treeType -> treeType.blockChecker(pos, level)).findFirst();
	}
}
