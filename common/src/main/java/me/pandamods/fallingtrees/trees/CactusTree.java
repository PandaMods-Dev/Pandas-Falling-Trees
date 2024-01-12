package me.pandamods.fallingtrees.trees;

import me.pandamods.fallingtrees.api.TreeType;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public class CactusTree implements TreeType {
	@Override
	public boolean baseBlockCheck(BlockState blockState) {
		return blockState.is(Blocks.CACTUS);
	}

	@Override
	public Set<BlockPos> blockGatheringAlgorithm(BlockPos blockPos, LevelAccessor level) {
		Set<BlockPos> blocks = new HashSet<>();
		loopBlocks(blockPos, level, blocks);
		return blocks;
	}

	private void loopBlocks(BlockPos pos, LevelAccessor level, Set<BlockPos> blocks) {
		blocks.add(pos);
		if (this.baseBlockCheck(level.getBlockState(pos.above()))) {
			loopBlocks(pos.above(), level, blocks);
		}
	}

	@Override
	public float fallAnimationEdgeDistance() {
		return 14f / 16f;
	}

	@Override
	public boolean enabled() {
		return !FallingTreesConfig.getCommonConfig().features.disableCactusTrees;
	}
}
