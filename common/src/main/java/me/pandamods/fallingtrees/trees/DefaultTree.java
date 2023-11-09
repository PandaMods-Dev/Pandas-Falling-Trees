package me.pandamods.fallingtrees.trees;

import dev.architectury.platform.Platform;
import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.api.TreeType;
import me.pandamods.fallingtrees.entity.TreeEntity;
import me.pandamods.fallingtrees.network.ConfigPacket;
import me.pandamods.fallingtrees.registry.SoundRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
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
	public void entityTick(TreeEntity entity) {
		TreeType.super.entityTick(entity);

		if (Platform.getEnv() == EnvType.CLIENT) {
			if (entity.tickCount == 1) {
				if (FallingTrees.getClientConfig().soundSettings.enabled) {
					entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.TREE_FALL.get(),
							SoundSource.BLOCKS, FallingTrees.getClientConfig().soundSettings.startVolume, 1f, true);
				}
			}

			if (entity.tickCount == entity.getMaxLifeTimeTick() / 2 - 10) {
				if (FallingTrees.getClientConfig().soundSettings.enabled) {
					entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.TREE_IMPACT.get(),
							SoundSource.BLOCKS, FallingTrees.getClientConfig().soundSettings.endVolume, 1f, true);
				}
			}
		}
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

	@Override
	public boolean allowedToFall(Player player) {
		return !(FallingTrees.getCommonConfig().isCrouchMiningAllowed &&
				player.isCrouching() != ConfigPacket.getClientConfig(player).getBoolean("invertCrouchMining"));
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
