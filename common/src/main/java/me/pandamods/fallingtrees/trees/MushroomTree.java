package me.pandamods.fallingtrees.trees;

import me.pandamods.fallingtrees.api.Tree;
import me.pandamods.fallingtrees.api.TreeData;
import me.pandamods.fallingtrees.api.TreeDataBuilder;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.config.common.tree.MushroomTreeConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Math;

import java.util.HashSet;
import java.util.Set;

public class MushroomTree implements Tree {
	@Override
	public boolean mineableBlock(BlockState blockState) {
		return getConfig().stemFilter.isValid(blockState);
	}

	@Override
	public TreeData getTreeData(TreeDataBuilder builder, BlockPos blockPos, BlockGetter level) {
		if (!this.mineableBlock(level.getBlockState(blockPos.above()))) return builder.build(false);

		Set<BlockPos> stemBlocks = new HashSet<>();
		Set<BlockPos> capBlocks = new HashSet<>();

		Set<BlockPos> loopedStemBlocks = new HashSet<>();
		loopStems(level, blockPos, stemBlocks, loopedStemBlocks);
		float speedMultiplication = FallingTreesConfig.getCommonConfig().dynamicMiningSpeed.speedMultiplication;
		float multiplyAmount = Math.min(FallingTreesConfig.getCommonConfig().dynamicMiningSpeed.maxSpeedMultiplication, ((float) stemBlocks.size() - 1f));
		builder.setMiningSpeed(1f / (multiplyAmount * speedMultiplication + 1f));

		stemBlocks.forEach(logPos -> {
			Set<BlockPos> loopedCapBlocks = new HashSet<>();
			for (Direction direction : Direction.values()) {
				BlockPos neighborPos = logPos.offset(direction.getNormal());
				loopCap(level, neighborPos, capBlocks, loopedCapBlocks);
			}
		});
		if (capBlocks.isEmpty()) return builder.build(false);

		Set<BlockPos> treeBlocks = new HashSet<>();
		treeBlocks.addAll(stemBlocks);
		treeBlocks.addAll(capBlocks);

		return builder
				.addBlocks(treeBlocks)
				.setAwardedBlocks(stemBlocks.size())
				.setFoodExhaustion(stemBlocks.size())
				.setToolDamage(stemBlocks.size())
				.build(true);
	}

	@Override
	public boolean allowedToFall(Player player) {
		return !(!FallingTreesConfig.getCommonConfig().disableCrouchMining &&
				player.isCrouching() != FallingTreesConfig.getClientConfig(player).invertCrouchMining);
	}

	public void loopStems(BlockGetter level, BlockPos blockPos, Set<BlockPos> blocks, Set<BlockPos> loopedBlocks) {
		if (loopedBlocks.contains(blockPos)) return;
		loopedBlocks.add(blockPos);

		BlockState blockState = level.getBlockState(blockPos);
		if (this.mineableBlock(blockState)) {
			blocks.add(blockPos);

			for (BlockPos offset : BlockPos.betweenClosed(new BlockPos(-1, 0, -1), new BlockPos(1, 1, 1))) {
				BlockPos neighborPos = blockPos.offset(offset);
				loopStems(level, neighborPos, blocks, loopedBlocks);
			}
		}
	}

	public void loopCap(BlockGetter level, BlockPos blockPos, Set<BlockPos> blocks, Set<BlockPos> loopedBlocks) {
		if (loopedBlocks.contains(blockPos)) return;
		loopedBlocks.add(blockPos);

		BlockState blockState = level.getBlockState(blockPos);
		if (getConfig().capFilter.isValid(blockState)) {
			blocks.add(blockPos);

			for (BlockPos offset : BlockPos.betweenClosed(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1))) {
				BlockPos neighborPos = blockPos.offset(offset);
				loopCap(level, neighborPos, blocks, loopedBlocks);
			}
		}
	}

	public MushroomTreeConfig getConfig() {
		return FallingTreesConfig.getCommonConfig().trees.mushroomTree;
	}

	@Override
	public boolean enabled() {
		return getConfig().enabled;
	}
}
