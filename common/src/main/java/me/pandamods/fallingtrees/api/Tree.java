package me.pandamods.fallingtrees.api;

import me.pandamods.fallingtrees.entity.TreeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Set;

public interface Tree {
	boolean mineableBlock(BlockState blockState);

	TreeData getTreeData(TreeDataBuilder builder, BlockPos blockPos, BlockGetter level);

	default boolean allowedTool(ItemStack itemStack, BlockState blockState) {
		return true;
	}

	default void entityTick(TreeEntity entity) {
		Level level = entity.level();
		if (entity.tickCount >= entity.getMaxLifeTimeTick()) {
			ItemStack usedItem = entity.getUsedTool();
			for (Map.Entry<BlockPos, BlockState> entry : entity.getBlocks().entrySet()) {
				if (shouldDropItems(usedItem, entry.getValue())) {
					BlockEntity blockEntity = null;
					if (entry.getValue().hasBlockEntity())
						blockEntity = level.getBlockEntity(entry.getKey().offset(entity.getOriginPos()));
					Block.dropResources(entry.getValue(), level, entity.getOriginPos(), blockEntity, entity.owner, usedItem);
				}
			}

			entity.remove(Entity.RemovalReason.DISCARDED);
		}
	}

	default boolean allowedToFall(Player player) {
		return true;
	}

	default boolean shouldDropItems(ItemStack itemStack, BlockState blockState) {
		return true;
	}

	default float fallAnimationEdgeDistance() {
		return 1;
	}

	default boolean enabled() {
		return true;
	}
}
