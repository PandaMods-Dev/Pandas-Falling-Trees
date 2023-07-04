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
		if (FallingTrees.serverConfig.allow_one_block_mining && TreeUtils.isMiningOneBlock(player))
			return;

		if (TreeUtils.isLog(blockState.getBlock())) {
			List<BlockPos> tree = TreeUtils.getTreeBlocks(blockPos, level);
			TreeUtils.getTreeBlocks(blockPos, level);

			Map<BlockPos, BlockState> treeBlocks = new HashMap<>();
			for (BlockPos pos : tree) {
				BlockState state = level.getBlockState(pos);
				treeBlocks.put(pos.subtract(blockPos), state);
			}

			if (treeBlocks.values().stream().anyMatch(state -> TreeUtils.isLeaves(state.getBlock()) &&
					(!(state.getBlock() instanceof LeavesBlock) || !state.getValue(LeavesBlock.PERSISTENT)))) {
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

				player.causeFoodExhaustion(0.005F*LogAmount);

				for (Map.Entry<BlockPos, BlockState> entry : treeBlocks.entrySet()) {
					player.awardStat(Stats.BLOCK_MINED.get(entry.getValue().getBlock()));
					level.setBlockAndUpdate(entry.getKey().offset(blockPos), Blocks.AIR.defaultBlockState());
				}
			}
		}
	}
}
