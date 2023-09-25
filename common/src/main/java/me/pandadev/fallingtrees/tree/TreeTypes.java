package me.pandadev.fallingtrees.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeTypes {
	public static Map<String, TreeType> TYPES = new HashMap<>();

	static {
		TYPES.put("tree", new TreeType(TreeTypes::treeConditions));
		TYPES.put("cactus", new TreeType(TreeTypes::cactusConditions));
	}

	private static boolean treeConditions(List<BlockPos> blockPos, Level level) {
	}

	private static boolean cactusConditions(List<BlockPos> blockPos, Level level) {
	}
}