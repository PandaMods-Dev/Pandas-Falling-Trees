package me.pandamods.fallingtrees.trees;

import dev.architectury.platform.Platform;
import me.pandamods.fallingtrees.api.Tree;
import me.pandamods.fallingtrees.config.ClientConfig;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.entity.TreeEntity;
import me.pandamods.fallingtrees.registry.SoundRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashSet;
import java.util.Set;

public class StandardTree extends Tree {
	@Override
	public boolean mineableBlock(BlockState blockState) {
		return FallingTreesConfig.getCommonConfig().trees.standardTree.logFilter.isValid(blockState);
	}

	public boolean extraRequiredBlockCheck(BlockState blockState) {
//		if (blockState.hasProperty(BlockStateProperties.PERSISTENT) && blockState.getValue(BlockStateProperties.PERSISTENT))
//			return false;
		return FallingTreesConfig.getCommonConfig().trees.standardTree.leavesFilter.isValid(blockState);
	}

	@Override
	public boolean allowedTool(ItemStack itemStack, BlockState blockState) {
		return itemStack.getItem() instanceof AxeItem || itemStack.is(ItemTags.AXES);
	}

	@Override
	public void entityTick(TreeEntity entity) {
		super.entityTick(entity);

		if (Platform.getEnv() == EnvType.CLIENT) {
			ClientConfig clientConfig = FallingTreesConfig.getClientConfig();
			if (entity.tickCount == 1) {
				if (clientConfig.soundSettings.enabled) {
					entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.TREE_FALL.get(),
							SoundSource.BLOCKS, clientConfig.soundSettings.startVolume, 1f, true);
				}
			}

			if (entity.tickCount == (int) (clientConfig.animation.fallAnimLength * 20) - 5) {
				if (clientConfig.soundSettings.enabled) {
					entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.TREE_IMPACT.get(),
							SoundSource.BLOCKS, clientConfig.soundSettings.endVolume, 1f, true);
				}
			}
		}
	}

	@Override
	public boolean blockGatheringAlgorithm(Set<BlockPos> blockList, BlockPos blockPos, LevelAccessor level) {
		if (!level.getBlockState(blockPos.above()).is(level.getBlockState(blockPos).getBlock())) return false;

		Set<BlockPos> logBlocks = new HashSet<>();
		Set<BlockPos> loopedLogBlocks = new HashSet<>();

		Set<BlockPos> leavesBlocks = new HashSet<>();
		Set<BlockPos> loopedLeavesBlocks = new HashSet<>();

		Set<BlockPos> decorationBlocks = new HashSet<>();
		Set<BlockPos> loopedDecorationBlocks = new HashSet<>();

		loopLogs(level, blockPos, logBlocks, loopedLogBlocks);

		blockList.addAll(logBlocks);
		blockList.addAll(leavesBlocks);
		blockList.addAll(decorationBlocks);
		return true;
	}

	@Override
	public boolean allowedToFall(Player player) {
		return !(FallingTreesConfig.getCommonConfig().isCrouchMiningAllowed &&
				player.isCrouching() != FallingTreesConfig.getClientConfig(player).invertCrouchMining);
	}

	public void loopLogs(LevelAccessor level, BlockPos blockPos, Set<BlockPos> logBlocks, Set<BlockPos> loopedLogBlocks) {
		BlockState blockState = level.getBlockState(blockPos);
		if (loopedLogBlocks.contains(blockPos))
			return;

		loopedLogBlocks.add(blockPos);

		if (this.mineableBlock(blockState)) {
			logBlocks.add(blockPos);

			for (BlockPos offset : BlockPos.betweenClosed(new BlockPos(-1, 0, -1), new BlockPos(1, 1, 1))) {
				BlockPos neighborPos = blockPos.offset(offset);
				loopLogs(level, neighborPos, logBlocks, loopedLogBlocks);
			}
		}
	}

	public void loopLogs(LevelAccessor level, BlockPos blockPos, Set<BlockPos> logBlocks, Set<BlockPos> loopedLogBlocks,
						 Set<BlockPos> leavesBlocks, Set<BlockPos> decorationBlocks, Set<BlockPos> loopedDecorationBlocks) {
		BlockState blockState = level.getBlockState(blockPos);
		if (loopedLogBlocks.contains(blockPos))
			return;

		loopedLogBlocks.add(blockPos);

		if (this.mineableBlock(blockState)) {
			logBlocks.add(blockPos);

			for (BlockPos offset : BlockPos.betweenClosed(new BlockPos(-1, 0, -1), new BlockPos(1, 1, 1))) {
				BlockPos neighborPos = blockPos.offset(offset);
				loopLogs(level, neighborPos, logBlocks, loopedLogBlocks, leavesBlocks, decorationBlocks, loopedDecorationBlocks);
			}
//			for (Direction direction : Direction.values()) {
//				BlockPos neighborPos = blockPos.offset(direction.getNormal());
//				loopDecorations(level, neighborPos, decorationBlocks, loopedDecorationBlocks);
//			}

			Set<BlockPos> loopedLeavesBlocks = new HashSet<>();

			for (Direction direction : Direction.values()) {
				BlockPos neighborPos = blockPos.offset(direction.getNormal());
				loopLeaves(level, neighborPos, 1, leavesBlocks, loopedLeavesBlocks, decorationBlocks, loopedDecorationBlocks);
			}
		}
	}

	public void loopLeaves(LevelAccessor level, BlockPos blockPos, int distance, Set<BlockPos> leavesBlocks, Set<BlockPos> loopedLeavesBlocks,
						   Set<BlockPos> decorationBlocks, Set<BlockPos> loopedDecorationBlocks) {
		BlockState blockState = level.getBlockState(blockPos);
		if ((blockState.hasProperty(BlockStateProperties.DISTANCE) && blockState.getValue(BlockStateProperties.DISTANCE) != distance) ||
				loopedLeavesBlocks.contains(blockPos))
			return;

		loopedLeavesBlocks.add(blockPos);

		if (this.extraRequiredBlockCheck(blockState)) {
			leavesBlocks.add(blockPos);

			for (Direction direction : Direction.values()) {
				BlockPos neighborPos = blockPos.offset(direction.getNormal());
				if (distance < FallingTreesConfig.getCommonConfig().trees.standardTree.maxLeavesRadius)
					loopLeaves(level, neighborPos, distance + 1, leavesBlocks, loopedLeavesBlocks, decorationBlocks, loopedDecorationBlocks);
//				loopDecorations(level, neighborPos, decorationBlocks, loopedDecorationBlocks);
			}
		}
	}

//	public void loopDecorations(LevelAccessor level, BlockPos blockPos, Set<BlockPos> decorationBlocks, Set<BlockPos> loopedDecorationBlocks) {
//		BlockState blockState = level.getBlockState(blockPos);
//		if (loopedDecorationBlocks.contains(blockPos))
//			return;
//
//		loopedDecorationBlocks.add(blockPos);
//
//
//		if (FallingTreesConfig.getCommonConfig().trees.standardTree.decorationBlocksFilter.isValid(blockState)) {
//			decorationBlocks.add(blockPos);
//
//			loopDecorations(level, blockPos.offset(Direction.DOWN.getNormal()), decorationBlocks, loopedDecorationBlocks);
//		}
//	}

	@Override
	public boolean enabled() {
		return FallingTreesConfig.getCommonConfig().trees.standardTree.enabled;
	}
}
