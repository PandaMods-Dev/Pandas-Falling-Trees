package me.pandamods.fallingtrees.trees;

import me.pandamods.fallingtrees.api.Tree;
import me.pandamods.fallingtrees.api.TreeData;
import me.pandamods.fallingtrees.api.TreeDataBuilder;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.config.common.tree.StandardTreeConfig;
import me.pandamods.fallingtrees.config.common.tree.VerticalTreeConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class VerticalTree implements Tree {
	@Override
	public boolean mineableBlock(BlockState blockState) {
		return getConfig().filter.isValid(blockState);
	}

	@Override
	public boolean allowedTool(ItemStack itemStack, BlockState blockState) {
		return !getConfig().onlyFallWithRequiredTool || getConfig().allowedToolFilter.isValid(itemStack);
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

	public VerticalTreeConfig getConfig() {
		return FallingTreesConfig.getCommonConfig().trees.verticalTree;
	}

	@Override
	public boolean enabled() {
		return getConfig().enabled;
	}
}
