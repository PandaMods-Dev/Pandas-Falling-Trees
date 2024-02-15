package me.pandamods.fallingtrees.trees;

import me.pandamods.fallingtrees.api.Tree;
import me.pandamods.fallingtrees.api.TreeData;
import me.pandamods.fallingtrees.api.TreeDataBuilder;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class VerticalTree implements Tree {
	public VerticalTree() {
		super();
	}

	@Override
	public boolean mineableBlock(BlockState blockState) {
		return FallingTreesConfig.getCommonConfig().trees.verticalTree.filter.isValid(blockState);
	}

	@Override
	public TreeData getTreeData(TreeDataBuilder builder, BlockPos blockPos, BlockGetter level) {
		loopBlocks(level.getBlockState(blockPos), blockPos, level, builder);
		return builder
				.setAwardedBlocks(builder.getBlocks().size())
				.setFoodExhaustion(builder.getBlocks().size())
				.setToolDamage(builder.getBlocks().size())
				.build(true);
	}

	private void loopBlocks(BlockState blockState, BlockPos blockpos, BlockGetter level, TreeDataBuilder builder) {
		builder.addBlock(blockpos);
		if (blockState.is(level.getBlockState(blockpos.above()).getBlock())) {
			loopBlocks(blockState, blockpos.above(), level, builder);
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
