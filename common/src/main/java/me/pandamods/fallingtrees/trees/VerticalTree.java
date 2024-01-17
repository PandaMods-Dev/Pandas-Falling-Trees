package me.pandamods.fallingtrees.trees;

import me.pandamods.fallingtrees.api.Tree;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class VerticalTree extends Tree {
	@Override
	public boolean mineableBlock(BlockState blockState) {
		return FallingTreesConfig.getCommonConfig().trees.verticalTree.filter.isValid(blockState);
	}

	@Override
	public boolean blockGatheringAlgorithm(Set<BlockPos> blockList, BlockPos blockPos, LevelAccessor level) {
		loopBlocks(level.getBlockState(blockPos), blockPos, level, blockList);
		return true;
	}

	private void loopBlocks(BlockState blockState, BlockPos blockpos, LevelAccessor level, Set<BlockPos> blocks) {
		blocks.add(blockpos);
		if (blockState.is(level.getBlockState(blockpos.above()).getBlock())) {
			loopBlocks(blockState, blockpos.above(), level, blocks);
		}
	}

	@Override
	public float fallAnimationEdgeDistance() {
		return 0;
	}

	@Override
	public boolean enabled() {
		return FallingTreesConfig.getCommonConfig().trees.verticalTree.enabled;
	}
}
