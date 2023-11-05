package me.pandamods.fallingtrees.trees;

import me.pandamods.fallingtrees.api.TreeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public class DefaultTree implements TreeType {
	@Override
	public boolean baseBlockCheck(BlockState blockState) {
		return blockState.is(BlockTags.LOGS);
	}

	@Override
	public boolean extraRequiredBlockCheck(BlockState blockState) {
		if (blockState.hasProperty(LeavesBlock.PERSISTENT) && blockState.getValue(LeavesBlock.PERSISTENT))
			return false;
		return blockState.getBlock() instanceof LeavesBlock;
	}

	@Override
	public boolean allowedTool(ItemStack itemStack, BlockState blockState) {
		return itemStack.is(ItemTags.AXES);
	}

	@Override
	public Set<BlockPos> blockGatheringAlgorithm(BlockPos blockPos, LevelAccessor level) {
		Set<BlockPos> blocks = new HashSet<>();

		Set<BlockPos> logBlocks = new HashSet<>();
		Set<BlockPos> loopedLogBlocks = new HashSet<>();

		Set<BlockPos> leavesBlocks = new HashSet<>();

		loopLogs(level, blockPos, logBlocks, loopedLogBlocks, leavesBlocks);

		blocks.addAll(logBlocks);
		blocks.addAll(leavesBlocks);
		return blocks;
	}

	public void loopLogs(LevelAccessor level, BlockPos originPos, Set<BlockPos> logBlocks, Set<BlockPos> loopedLogBlocks, Set<BlockPos> leavesBlocks) {
		if (loopedLogBlocks.contains(originPos))
			return;

		loopedLogBlocks.add(originPos);

		BlockState blockState = level.getBlockState(originPos);
		if (this.baseBlockCheck(blockState)) {
			logBlocks.add(originPos);

			for (BlockPos offset : BlockPos.betweenClosed(new BlockPos(-1, 0, -1), new BlockPos(1, 1, 1))) {
				BlockPos neighborPos = originPos.offset(offset);
				loopLogs(level, neighborPos, logBlocks, loopedLogBlocks, leavesBlocks);
			}

			Set<BlockPos> loopedLeavesBlocks = new HashSet<>();

			for (Direction direction : Direction.values()) {
				BlockPos neighborPos = originPos.offset(direction.getNormal());
				loopLeaves(level, neighborPos, 1, leavesBlocks, loopedLeavesBlocks);
			}
		}
	}

	public void loopLeaves(LevelAccessor level, BlockPos originPos, int distance, Set<BlockPos> leavesBlocks, Set<BlockPos> loopedLeavesBlocks) {
		BlockState blockState = level.getBlockState(originPos);
		if ((blockState.hasProperty(LeavesBlock.DISTANCE) && blockState.getValue(LeavesBlock.DISTANCE) != distance) ||
				distance >= 7 || loopedLeavesBlocks.contains(originPos))
			return;

		loopedLeavesBlocks.add(originPos);

		if (this.extraRequiredBlockCheck(blockState)) {
			leavesBlocks.add(originPos);

			for (Direction direction : Direction.values()) {
				BlockPos neighborPos = originPos.offset(direction.getNormal());
				loopLeaves(level, neighborPos, distance + 1, leavesBlocks, loopedLeavesBlocks);
			}
		}
	}
}
