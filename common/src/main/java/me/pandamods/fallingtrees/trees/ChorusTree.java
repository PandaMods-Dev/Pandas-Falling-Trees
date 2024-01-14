package me.pandamods.fallingtrees.trees;

import me.pandamods.fallingtrees.api.Tree;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public class ChorusTree implements Tree {
	@Override
	public boolean mineableBlock(BlockState blockState) {
		return blockState.is(Blocks.CHORUS_PLANT);
	}

	@Override
	public boolean extraRequiredBlockCheck(BlockState blockState) {
		return blockState.is(Blocks.CHORUS_FLOWER);
	}

	@Override
	public boolean blockGatheringAlgorithm(Set<BlockPos> blockList, BlockPos blockPos, LevelAccessor level) {
		Set<BlockPos> loopedBlocks = new HashSet<>();

		loopBlocks(level, blockPos, blockList, loopedBlocks);
		return true;
	}

	public void loopBlocks(LevelAccessor level, BlockPos originPos, Set<BlockPos> blocks, Set<BlockPos> loopedBlocks) {
		if (loopedBlocks.contains(originPos))
			return;

		loopedBlocks.add(originPos);

		BlockState blockState = level.getBlockState(originPos);
		if (this.mineableBlock(blockState) || this.extraRequiredBlockCheck(blockState)) {
			blocks.add(originPos);

			if (this.mineableBlock(blockState)) {
				for (Direction direction : Direction.values()) {
					if (blockState.getValue(ChorusPlantBlock.PROPERTY_BY_DIRECTION.get(direction))) {
						BlockPos neighborPos = originPos.offset(direction.getNormal());
						loopBlocks(level, neighborPos, blocks, loopedBlocks);
					}
				}
			}
		}
	}

	@Override
	public float fallAnimationEdgeDistance() {
		return 6f / 16f;
	}

	@Override
	public boolean shouldDropItems(BlockState blockState) {
		return blockState.is(Blocks.CHORUS_FLOWER);
	}

	@Override
	public boolean enabled() {
		return !FallingTreesConfig.getCommonConfig().features.disableChorusTrees;
	}
}
