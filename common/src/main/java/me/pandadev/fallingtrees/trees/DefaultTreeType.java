package me.pandadev.fallingtrees.trees;

import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.tree.TreeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class DefaultTreeType extends TreeType {
	@Override
	public boolean blockChecker(BlockPos pos, BlockGetter level) {
		BlockState state = level.getBlockState(pos);
		return DefaultTreeType.isLog(state.getBlock());
	}

	@Override
	public boolean extraBlockRequirement(Map<BlockPos, BlockState> blocks, BlockGetter level) {
		return blocks.entrySet().stream().anyMatch(entry -> DefaultTreeType.isLeaves(entry.getValue().getBlock()));
	}

	@Override
	public void onFall(Player player, BlockPos pos, Level level) {
		if (FallingTrees.getClientConfig().sound_effect) {
			level.playSound(player, pos, FallingTrees.TREE_FALL.get(), SoundSource.BLOCKS, FallingTrees.getClientConfig().sound_effect_volume, 1);
		}
	}

	@Override
	public List<BlockPos> blockDetectionAlgorithm(BlockPos startPos, BlockGetter level) {
		List<BlockPos> logBlocks = new ArrayList<>();
		List<BlockPos> leafBlocks = new ArrayList<>();

        Set<BlockPos> visitedLogs = new HashSet<>();
        Map<BlockPos, Set<BlockPos>> visitedLeaves = new HashMap<>();

        getLogBlocksRecursive(startPos, level, logBlocks, visitedLogs);
		for (BlockPos logBlock : logBlocks) {
			visitedLeaves.put(logBlock, new HashSet<>());
			for (Direction direction : Direction.values()) {
				BlockPos neighborPos = logBlock.offset(direction.getNormal());
				if (DefaultTreeType.isLeaves(level.getBlockState(neighborPos).getBlock()))
					getLeavesBlocksRecursive(neighborPos, logBlock, level, leafBlocks, visitedLeaves);
			}
		}

		List<BlockPos> treeBlocks = new ArrayList<>();
		treeBlocks.addAll(logBlocks);
		treeBlocks.addAll(leafBlocks);
        return treeBlocks;
	}

	private static void getLogBlocksRecursive(BlockPos pos, BlockGetter level, List<BlockPos> treeBlocks, Set<BlockPos> visited) {
		BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if (visited.contains(pos)) {
            return;
        }

        visited.add(pos);

        if (DefaultTreeType.isLog(block)) {
            treeBlocks.add(pos);

            for (BlockPos offset : BlockPos.betweenClosed(new BlockPos(-1, 0, -1), new BlockPos(1, 1, 1))) {
                BlockPos neighborPos = pos.offset(offset);
                getLogBlocksRecursive(neighborPos, level, treeBlocks, visited);
            }
        }
    }

	private static void getLeavesBlocksRecursive(BlockPos pos, BlockPos logPos, BlockGetter level, List<BlockPos> leavesBlock, Map<BlockPos, Set<BlockPos>> visited) {
		BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if (!DefaultTreeType.isLeaves(block) || (block instanceof LeavesBlock && state.getValue(LeavesBlock.DISTANCE) != logPos.distManhattan(pos)) ||
				visited.get(logPos).contains(pos)) {
            return;
        }

        visited.get(logPos).add(pos);

		if (!leavesBlock.contains(pos))
        	leavesBlock.add(pos);

		for (BlockPos offset : BlockPos.betweenClosed(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1))) {
			BlockPos neighborPos = pos.offset(offset);
			getLeavesBlocksRecursive(neighborPos, logPos, level, leavesBlock, visited);
		}
    }

	public static boolean isLog(Block block) {
		if (FallingTrees.getServerConfig().block_acceptance.blacklisted_log_blocks.contains(BuiltInRegistries.BLOCK.getKey(block).toString()))
			return false;
		if (FallingTrees.getServerConfig().block_acceptance.whitelisted_log_blocks.contains(BuiltInRegistries.BLOCK.getKey(block).toString()))
			return true;

		return block.defaultBlockState().getTags().anyMatch(blockTagKey ->
				FallingTrees.getServerConfig().block_acceptance.whitelisted_log_block_tags.contains(blockTagKey.location().toString()));
	}

	public static boolean isLeaves(Block block) {
		if (FallingTrees.getServerConfig().block_acceptance.blacklisted_leaves_blocks.contains(BuiltInRegistries.BLOCK.getKey(block).toString()))
			return false;
		if (FallingTrees.getServerConfig().block_acceptance.whitelisted_leaves_blocks.contains(BuiltInRegistries.BLOCK.getKey(block).toString()))
			return true;

		return block.defaultBlockState().getTags().anyMatch(blockTagKey ->
				FallingTrees.getServerConfig().block_acceptance.whitelisted_leaves_block_tags.contains(blockTagKey.location().toString()));
	}
}
