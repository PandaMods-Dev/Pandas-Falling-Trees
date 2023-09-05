package me.pandadev.fallingtrees.tree;

import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.FallingTreesConfig;
import me.pandadev.fallingtrees.entity.TreeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;

import java.util.*;

public class TreeUtils {
	public static boolean isLog(Block block) {
		if (FallingTrees.serverConfig.blacklisted_log_blocks.contains(BuiltInRegistries.BLOCK.getKey(block).toString()))
			return false;
		if (FallingTrees.serverConfig.whitelisted_log_blocks.contains(BuiltInRegistries.BLOCK.getKey(block).toString()))
			return true;

		return block.defaultBlockState().getTags().anyMatch(blockTagKey ->
				FallingTrees.serverConfig.whitelisted_log_block_tags.contains(blockTagKey.location().toString()));
	}

	public static boolean isLeaves(Block block) {
		if (FallingTrees.serverConfig.blacklisted_leaves_blocks.contains(BuiltInRegistries.BLOCK.getKey(block).toString()))
			return false;
		if (FallingTrees.serverConfig.whitelisted_leaves_blocks.contains(BuiltInRegistries.BLOCK.getKey(block).toString()))
			return true;

		return block.defaultBlockState().getTags().anyMatch(blockTagKey ->
				FallingTrees.serverConfig.whitelisted_leaves_block_tags.contains(blockTagKey.location().toString()));
	}

	@Environment(EnvType.CLIENT)
	public static boolean isMiningOneBlock(Player player) {
		if (FallingTrees.configHolder.getConfig().one_block_mining_method.equals(FallingTreesConfig.OneBlockMiningEnum.CROUCH)) {
			return player.isCrouching();
		}
		return FallingTrees.configHolder.getConfig().is_mining_one_block;
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

//	public static boolean hasTag(TagKey<Block> tag, Block block) {
//		return block.defaultBlockState().getTags().anyMatch(blockTagKey -> blockTagKey.equals(tag));
//	}

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
        if (!isLeaves(block) || (block instanceof LeavesBlock && state.getValue(LeavesBlock.DISTANCE) != logPos.distManhattan(pos)) ||
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

	public static int getAmountOfLogs(Map<BlockPos, BlockState> blocks) {
		return (int) blocks.values().stream().filter(state1 -> TreeUtils.isLog(state1.getBlock())).count();
	}

	public static void breakTree(Player player, Level level, BlockPos blockPos) {
		BlockState blockState = level.getBlockState(blockPos);
		if (TreeUtils.isLog(blockState.getBlock())) {
			List<BlockPos> tree = TreeUtils.getTreeBlocks(blockPos, level);

			Map<BlockPos, BlockState> treeBlocks = new HashMap<>();
			for (BlockPos pos : tree) {
				BlockState state = level.getBlockState(pos);
				treeBlocks.put(pos.subtract(blockPos), state);
			}

			if (treeBlocks.values().stream().anyMatch(state -> TreeUtils.isLeaves(state.getBlock()) &&
					(!(state.getBlock() instanceof LeavesBlock) || !state.getValue(LeavesBlock.PERSISTENT)))) {
				TreeEntity treeEntity = new TreeEntity(FallingTrees.TREE_ENTITY.get(), level).setBlocks(treeBlocks);
				Vector3d position = new Vector3d(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
				treeEntity.setPos(position.x, position.y, position.z);
				ItemStack usedItem = player.getMainHandItem();
				treeEntity.usedItem = usedItem;

				treeEntity.setRotationY((float) Math.atan2(player.getX() - position.x, player.getZ() - position.z));
				level.addFreshEntity(treeEntity);
				if (FallingTrees.serverConfig.sound_effect)
					level.playSound(null, blockPos, FallingTrees.TREE_FALL.get(), SoundSource.BLOCKS,
							0.25f*FallingTrees.serverConfig.sound_effect_volume, 1);

				int LogAmount = TreeUtils.getAmountOfLogs(treeBlocks);
				if (usedItem.isDamageableItem()) {
					usedItem.hurtAndBreak((int) (LogAmount * FallingTrees.serverConfig.item_damage_multiplier), player, player1 -> {});
				}

				player.causeFoodExhaustion(0.005F * LogAmount * FallingTrees.serverConfig.food_exhaustion_multiplier);

				for (Map.Entry<BlockPos, BlockState> entry : treeBlocks.entrySet()) {
					player.awardStat(Stats.BLOCK_MINED.get(entry.getValue().getBlock()));
					level.setBlockAndUpdate(entry.getKey().offset(blockPos), Blocks.AIR.defaultBlockState());
				}
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public static boolean shouldTreeFall(Player player) {
		if (FallingTrees.serverConfig.tree_limit.only_fall_on_tool_use) {
			return player.getMainHandItem().getItem() instanceof AxeItem && !(FallingTrees.serverConfig.allow_one_block_mining && TreeUtils.isMiningOneBlock(player));
		}
		return !(FallingTrees.serverConfig.allow_one_block_mining && TreeUtils.isMiningOneBlock(player));
	}
}
