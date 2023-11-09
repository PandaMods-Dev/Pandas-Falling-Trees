package me.pandamods.fallingtrees.api;

import dev.architectury.platform.Platform;
import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.entity.TreeEntity;
import me.pandamods.fallingtrees.registry.SoundRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
		Level level = entity.level();
		if (entity.tickCount >= entity.getLifeTime()) {
			ItemStack usedItem = entity.getUsedTool();
			for (Map.Entry<BlockPos, BlockState> entry : entity.getBlocks().entrySet()) {
				BlockEntity blockEntity = null;
				if (entry.getValue().hasBlockEntity()) blockEntity = level.getBlockEntity(entry.getKey().offset(entity.getOriginPos()));
				Block.dropResources(entry.getValue(), level, entity.getOriginPos(), blockEntity, entity.owner, usedItem);
			}

			entity.remove(Entity.RemovalReason.DISCARDED);
		}

		if (Platform.getEnv() == EnvType.CLIENT) {
			if (entity.tickCount == 1) {
				if (FallingTrees.getClientConfig().playSoundEffect) {
					level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.TREE_FALL.get(),
							SoundSource.BLOCKS, 1f, 1f, true);
				}
			}

//			if (entity.tickCount == entity.getLifeTime() / 2) {
//				if (FallingTrees.getClientConfig().playSoundEffect) {
//					level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundRegistry.TREE_IMPACT.get(),
//							SoundSource.BLOCKS, 1f, 1f, true);
//				}
//			}
		}
	}
}
