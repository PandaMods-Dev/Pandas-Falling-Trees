package me.pandamods.fallingtrees.api;

import me.pandamods.fallingtrees.entity.TreeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Set;

public interface TreeType {
	boolean baseBlockCheck(BlockState blockState);

	Set<BlockPos> blockGatheringAlgorithm(BlockPos blockPos, LevelAccessor level);

	default boolean extraRequiredBlockCheck(BlockState blockState) {
		return true;
	}

	default boolean mineableBlock(BlockState blockState) {
		return baseBlockCheck(blockState);
	}

	default boolean allowedTool(ItemStack itemStack, BlockState blockState) {
		return true;
	}

	default void entityTick(TreeEntity entity) {
		if (entity.tickCount >= entity.getLifeTime()) {
			ItemStack usedItem = entity.getUsedTool();
			for (Map.Entry<BlockPos, BlockState> entry : entity.getBlocks().entrySet()) {
				BlockEntity blockEntity = null;
				if (entry.getValue().hasBlockEntity()) blockEntity = entity.level().getBlockEntity(entry.getKey().offset(entity.getOriginPos()));
				Block.dropResources(entry.getValue(), entity.level(), entity.getOriginPos(), blockEntity, entity.owner, usedItem);
			}

			entity.remove(Entity.RemovalReason.DISCARDED);
		}
	}
}
