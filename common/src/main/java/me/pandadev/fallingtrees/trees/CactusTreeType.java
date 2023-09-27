package me.pandadev.fallingtrees.trees;

import me.pandadev.fallingtrees.tree.TreeType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class CactusTreeType extends TreeType {
	@Override
	public boolean blockChecker(BlockPos pos, BlockGetter level) {
		BlockState state = level.getBlockState(pos);
		return state.is(Blocks.CACTUS);
	}

	@Override
	public List<BlockPos> blockDetectionAlgorithm(BlockPos pos, BlockGetter level) {
		List<BlockPos> blocks = new ArrayList<>();
		getBlocksRecursive(pos, level, blocks);
		return blocks;
	}

	private void getBlocksRecursive(BlockPos pos, BlockGetter level, List<BlockPos> treeBlocks) {
		treeBlocks.add(pos);
		if (this.blockChecker(pos.above(), level)) {
			getBlocksRecursive(pos.above(), level, treeBlocks);
		}
	}
}
