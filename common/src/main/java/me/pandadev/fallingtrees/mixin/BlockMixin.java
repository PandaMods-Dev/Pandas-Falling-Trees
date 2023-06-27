package me.pandadev.fallingtrees.mixin;

import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.entity.TreeEntity;
import me.pandadev.fallingtrees.tree.TreeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(Block.class)
public abstract class BlockMixin {
	@Inject(method = "playerWillDestroy",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V"
			),
			cancellable = true)
	public void blockMine(Level level, BlockPos blockPos, BlockState blockState, Player player, CallbackInfo ci) {
		if (FallingTrees.configHolder.getConfig().allow_one_block_mining && player.isCrouching())
			return;

		if (TreeUtils.isLog(blockState.getBlock())) {
			List<BlockPos> tree = TreeUtils.getTreeBlocks(blockPos, level);
			TreeUtils.getTreeBlocks(blockPos, level);

			Map<BlockPos, BlockState> treeBlocks = new HashMap<>();
			for (BlockPos pos : tree) {
				BlockState state = level.getBlockState(pos);
				treeBlocks.put(pos.subtract(blockPos), state);
			}

			if (treeBlocks.values().stream().anyMatch(state -> TreeUtils.isLeaves(state.getBlock()) && !state.getValue(LeavesBlock.PERSISTENT))) {
				ci.cancel();
				TreeEntity treeEntity = new TreeEntity(FallingTrees.TREE_ENTITY.get(), level).setBlocks(treeBlocks);
				Vector3d position = new Vector3d(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
				treeEntity.setPos(position.x, position.y, position.z);
				ItemStack usedItem = player.getMainHandItem();
				treeEntity.usedItem = usedItem;
				treeEntity.setRotationY((float) Math.atan2(player.getX() - position.x, player.getZ() - position.z));
				level.addFreshEntity(treeEntity);

				int LogAmount = (int) treeBlocks.values().stream().filter(state1 -> !TreeUtils.isLeaves(state1.getBlock())).count();
				if (usedItem.isDamageableItem()) {
					usedItem.hurtAndBreak(LogAmount-1, player, player1 -> {});
				}
				player.awardStat(Stats.BLOCK_MINED.get((Block) (Object)this));
        		player.causeFoodExhaustion(0.005F*LogAmount);

				for (Map.Entry<BlockPos, BlockState> entry : treeBlocks.entrySet()) {
					level.setBlockAndUpdate(entry.getKey().offset(blockPos), Blocks.AIR.defaultBlockState());
				}
			}
		}
	}

//	@Inject(method = "playerWillDestroy",
//			at = @At(
//					value = "INVOKE",
//					target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V"
//			),
//			cancellable = true)
//	public void destroy(Level level, BlockPos pos, BlockState state, Player player, CallbackInfo ci) {
//		if (TreesConfig.isValidLog(state.getBlock())) {
//			Map<BlockPos, BlockState> blocks = new HashMap<>();
//
//			addTreeBlock(level, pos, pos, pos, blocks, state.getBlock());
//
//			if (blocks.values().stream().anyMatch(state1 -> state1.getBlock() instanceof LeavesBlock && !state1.getValue(LeavesBlock.PERSISTENT))) {
//
//				TreeEntity treeEntity = new TreeEntity(FallingTrees.TREE_ENTITY.get(), level).setBlocks(blocks);
//				Vector3d position = new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
//				treeEntity.setPos(position.x, position.y, position.z);
//				ItemStack usedItem = player.getMainHandItem();
//				treeEntity.usedItem = usedItem;
//				treeEntity.setRotationY((float) Math.atan2(player.getX() - position.x, player.getZ() - position.z));
//				level.addFreshEntity(treeEntity);
//
//				int LogAmount = (int) blocks.values().stream().filter(state1 -> !(state1.getBlock() instanceof LeavesBlock)).count();
//				if (usedItem.isDamageableItem()) {
//					usedItem.hurtAndBreak(LogAmount, player, player1 -> {});
//				}
//
//				ci.cancel();
//				for (Map.Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
//					level.removeBlock(new BlockPos(
//							entry.getKey().getX() + pos.getX(),
//							entry.getKey().getY() + pos.getY(),
//							entry.getKey().getZ() + pos.getZ()
//					), false);
//				}
//			}
//		}
//	}

//	private void addTreeBlock(Level level, BlockPos pos, BlockPos basePos, BlockPos lastLog, Map<BlockPos, BlockState> blocks, Block log) {
//        BlockState state = level.getBlockState(pos);
//        Block block = state.getBlock();
//
//		boolean isLog = state.is(log);
//		boolean isLeave = block instanceof LeavesBlock;
//		int leaveDistance;
//
//        if (isLog || isLeave) {
//			if (isLeave) {
//				leaveDistance = state.getValue(LeavesBlock.DISTANCE);
//				if (lastLog.distManhattan(pos) > leaveDistance + 1)
//					return;
//			}
//			if (isLog) {
//				if (lastLog.distManhattan(pos) > 1 && isLogConnectedToGround(level, pos))
//					return;
//				lastLog = pos;
//			}
//
//			blocks.put(pos.subtract(basePos), state);
//
//            for (BlockPos neighborPos : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 1, 1))) {
//				if (!blocks.containsKey(neighborPos.subtract(basePos)))
//					addTreeBlock(level, neighborPos, basePos, lastLog, blocks, log);
//            }
//        }
//    }
}
