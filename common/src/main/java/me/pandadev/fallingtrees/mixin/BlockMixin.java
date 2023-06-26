package me.pandadev.fallingtrees.mixin;

import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.TreesConfig;
import me.pandadev.fallingtrees.entity.TreeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(Block.class)
public abstract class BlockMixin {
	@Inject(method = "playerWillDestroy",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V"
			),
			cancellable = true)
	public void destroy(Level level, BlockPos pos, BlockState state, Player player, CallbackInfo ci) {
		System.out.println("test1");
		if (TreesConfig.isValidLog(state.getBlock())) {
			System.out.println("test2");
			Map<BlockPos, BlockState> blocks = new HashMap<>();

			addTreeBlock(level, pos, pos, pos, blocks, state.getBlock());

			if (blocks.values().stream().anyMatch(state1 -> state1.getBlock() instanceof LeavesBlock && !state1.getValue(LeavesBlock.PERSISTENT))) {

				TreeEntity treeEntity = new TreeEntity(FallingTrees.TREE_ENTITY.get(), level).setBlocks(blocks);
				Vector3d position = new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				treeEntity.setPos(position.x, position.y, position.z);
				ItemStack usedItem = player.getMainHandItem();
				treeEntity.usedItem = usedItem;
				treeEntity.setRotationY((float) Math.atan2(player.getX() - position.x, player.getZ() - position.z));
				level.addFreshEntity(treeEntity);

				int LogAmount = (int) blocks.values().stream().filter(state1 -> !(state1.getBlock() instanceof LeavesBlock)).count();
				if (usedItem.isDamageableItem()) {
					usedItem.hurtAndBreak(LogAmount, player, player1 -> {});
				}

				ci.cancel();
				for (Map.Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
					level.removeBlock(new BlockPos(
							entry.getKey().getX() + pos.getX(),
							entry.getKey().getY() + pos.getY(),
							entry.getKey().getZ() + pos.getZ()
					), false);
				}
			}
		}
	}

	private void addTreeBlock(Level level, BlockPos pos, BlockPos basePos, BlockPos lastLog, Map<BlockPos, BlockState> blocks, Block log) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

		boolean isLog = state.is(log);
		boolean isLeave = block instanceof LeavesBlock;
		int leaveDistance;

        if (isLog || isLeave) {
			if (isLeave) {
				leaveDistance = state.getValue(LeavesBlock.DISTANCE);
				if (lastLog.distManhattan(pos) > leaveDistance + 1)
					return;
			}
			if (isLog) {
				if (lastLog.distManhattan(pos) > 1 && isLogConnectedToGround(level, pos))
					return;
				lastLog = pos;
			}

			blocks.put(pos.subtract(basePos), state);

            for (BlockPos neighborPos : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 1, 1))) {
				if (!blocks.containsKey(neighborPos.subtract(basePos)))
					addTreeBlock(level, neighborPos, basePos, lastLog, blocks, log);
            }
        }
    }

	public boolean isLogConnectedToGround(LevelAccessor level, BlockPos pos) {
		BlockState blockBelow = level.getBlockState(pos.below());
		if (blockBelow.getBlock() instanceof LeavesBlock || blockBelow.isAir()) {
			return false;
		} else if (blockBelow.is(level.getBlockState(pos).getBlock())) {
			return isLogConnectedToGround(level, pos.below());
		}
		return true;
	}
}
