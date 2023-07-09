package me.pandadev.fallingtrees.tree;

import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.FallingTreesConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public record TreeCache(List<BlockPos> treeBlocks, Level level) {
	public static TreeCache cache = null;

	public int getLogAmount() {
		int amount = 0;
		for (BlockPos treeBlock : treeBlocks) {
			if (TreeUtils.isLog(level.getBlockState(treeBlock).getBlock())) amount++;
		}
		return amount;
	}

	public boolean isTreeSizeToBig() {
		if (FallingTrees.serverConfig.tree_limit.tree_limit_method.equals(FallingTreesConfig.TreeLimit.LimitMethodEnum.BLOCKS)) {
			return treeBlocks.size() > FallingTrees.serverConfig.tree_limit.tree_size_limit;
		}
		return treeBlocks.stream().filter(blockPos ->
				TreeUtils.isLog(level.getBlockState(blockPos).getBlock())).count() > FallingTrees.serverConfig.tree_limit.tree_size_limit;
	}

	public static TreeCache getOrCreateCache(BlockPos pos, Level level) {
		if (TreeCache.cache == null || TreeCache.cache.treeBlocks().get(0) != pos) {
			return TreeCache.createCache(pos, level);
		}
		return cache;
	}

	public static TreeCache createCache(BlockPos pos, Level level) {
		return TreeCache.cache = new TreeCache(TreeUtils.getTreeBlocks(pos, level), level);
	}
}
