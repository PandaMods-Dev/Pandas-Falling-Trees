package me.pandadev.fallingtrees.tree;

import me.pandadev.fallingtrees.FallingTrees;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class TreeUtils {
	public static boolean isLog(Block block) {
		if (Arrays.stream(FallingTrees.configHolder.getConfig().blacklisted_log_blocks).anyMatch(s -> s.equals(BuiltInRegistries.BLOCK.getKey(block).toString())))
			return false;
		return hasTag(BlockTags.LOGS, block);
	}

	public static boolean isLeaves(Block block) {
		if (Arrays.stream(FallingTrees.configHolder.getConfig().blacklisted_leaves_blocks).anyMatch(s -> s.equals(BuiltInRegistries.BLOCK.getKey(block).toString())))
			return false;
		return hasTag(BlockTags.LEAVES, block);
	}

//	public static boolean isDecorative(Block block) {
//		List<TagKey<Block>> tags = block.defaultBlockState().getTags().toList();
//		for (String validDecorativeBlockTag : FallingTrees.configHolder.getConfig().valid_decorative_block_tags) {
//			if (tags.stream().anyMatch(blockTagKey -> validDecorativeBlockTag.equals(blockTagKey.location().toString()))) {
//				return true;
//			}
//		}
//		return Arrays.stream(FallingTrees.configHolder.getConfig().valid_decorative_blocks).anyMatch(s -> s.equals(BuiltInRegistries.BLOCK.getKey(block).toString()));
//	}

	public static boolean hasTag(TagKey<Block> tag, Block block) {
		return block.defaultBlockState().getTags().anyMatch(blockTagKey -> blockTagKey.equals(tag));
	}

	public static List<BlockPos> getTreeBlocks(BlockPos startPos, Level level) {
        List<BlockPos> logBlocks = new ArrayList<>();
		List<BlockPos> leafBlocks = new ArrayList<>();
		List<BlockPos> decorativeBlocks = new ArrayList<>();

        Set<BlockPos> visitedLogs = new HashSet<>();
        Map<BlockPos, Set<BlockPos>> visitedLeaves = new HashMap<>();
		Set<BlockPos> visitedDecorative = new HashSet<>();

        getLogBlocksRecursive(startPos, level, logBlocks, visitedLogs);
		for (BlockPos logBlock : logBlocks) {
			visitedLeaves.put(logBlock, new HashSet<>());
			for (Direction direction : Direction.values()) {
				BlockPos neighborPos = logBlock.offset(direction.getNormal());
				if (isLeaves(level.getBlockState(neighborPos).getBlock()))
					getLeavesBlocksRecursive(neighborPos, logBlock, level, leafBlocks, visitedLeaves);
			}
		}

		List<BlockPos> treeBlocks = new ArrayList<>();
		treeBlocks.addAll(logBlocks);
		treeBlocks.addAll(leafBlocks);

//		for (BlockPos blockPos : new ArrayList<>(treeBlocks)) {
//			for (Direction direction : Direction.values()) {
//				BlockPos neighborPos = blockPos.offset(direction.getNormal());
//				getDecorativeBlocksRecursive(neighborPos, level, decorativeBlocks, visitedDecorative);
//			}
//		}
//		treeBlocks.addAll(decorativeBlocks);

        return treeBlocks;
    }

	private static void getLogBlocksRecursive(BlockPos pos, Level level, List<BlockPos> treeBlocks, Set<BlockPos> visited) {
        if (visited.contains(pos)) {
            return;
        }

        visited.add(pos);
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (isLog(block)) {
            treeBlocks.add(pos);

            for (BlockPos offset : BlockPos.betweenClosed(new BlockPos(-1, 0, -1), new BlockPos(1, 1, 1))) {
                BlockPos neighborPos = pos.offset(offset);
                getLogBlocksRecursive(neighborPos, level, treeBlocks, visited);
            }
        }
    }

	private static void getLeavesBlocksRecursive(BlockPos pos, BlockPos logPos, Level level, List<BlockPos> leavesBlock, Map<BlockPos, Set<BlockPos>> visited) {
		BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if (!isLeaves(block) || state.getValue(LeavesBlock.DISTANCE) != logPos.distManhattan(pos) || visited.get(logPos).contains(pos)) {
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

//	private static void getDecorativeBlocksRecursive(BlockPos pos, Level level, List<BlockPos> decorativeBlock, Set<BlockPos> visited) {
//        if (visited.contains(pos)) {
//            return;
//        }
//
//        visited.add(pos);
//		BlockState state = level.getBlockState(pos);
//        Block block = state.getBlock();
//
//		if (isDecorative(block)) {
//			decorativeBlock.add(pos);
//
//			getDecorativeBlocksRecursive(pos.above(), level, decorativeBlock, visited);
//			getDecorativeBlocksRecursive(pos.below(), level, decorativeBlock, visited);
//		}
//    }

	public static boolean isLogConnectedToGround(LevelAccessor level, BlockPos pos) {
		BlockState blockBelow = level.getBlockState(pos.below());
		if (blockBelow.getBlock() instanceof LeavesBlock || blockBelow.isAir()) {
			return false;
		} else if (blockBelow.is(level.getBlockState(pos).getBlock())) {
			return isLogConnectedToGround(level, pos.below());
		}
		return true;
	}
}
