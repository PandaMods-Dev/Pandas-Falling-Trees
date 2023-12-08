package me.pandamods.fallingtrees.trees;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.api.TreeType;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.network.ConfigPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public class ChorusTree implements TreeType {
	@Override
	public boolean baseBlockCheck(BlockState blockState) {
		return blockState.is(Blocks.CHORUS_PLANT);
	}

	@Override
	public boolean extraRequiredBlockCheck(BlockState blockState) {
		return blockState.is(Blocks.CHORUS_FLOWER);
	}

	@Override
	public Set<BlockPos> blockGatheringAlgorithm(BlockPos blockPos, LevelAccessor level) {
		Set<BlockPos> blocks = new HashSet<>();
		Set<BlockPos> loopedBlocks = new HashSet<>();

		loopBlocks(level, blockPos, blocks, loopedBlocks);
		return blocks;
	}

	public void loopBlocks(LevelAccessor level, BlockPos originPos, Set<BlockPos> blocks, Set<BlockPos> loopedBlocks) {
		if (loopedBlocks.contains(originPos))
			return;

		loopedBlocks.add(originPos);

		BlockState blockState = level.getBlockState(originPos);
		if (this.baseBlockCheck(blockState) || this.extraRequiredBlockCheck(blockState)) {
			blocks.add(originPos);

			if (this.baseBlockCheck(blockState)) {
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
