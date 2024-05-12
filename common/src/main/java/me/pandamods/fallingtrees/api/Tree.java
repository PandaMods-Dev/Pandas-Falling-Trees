package me.pandamods.fallingtrees.api;

import dev.architectury.hooks.level.entity.ItemEntityHooks;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.config.common.tree.StandardTreeConfig;
import me.pandamods.fallingtrees.config.common.tree.TreeConfig;
import me.pandamods.fallingtrees.entity.TreeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Tree<T extends TreeConfig> {
	boolean mineableBlock(BlockState blockState);
	
	TreeData getTreeData(TreeDataBuilder builder, BlockPos blockPos, BlockGetter level);

	default void entityTick(TreeEntity entity) {
		Level level = entity.level;
		if (entity.tickCount >= entity.getMaxLifeTimeTick()) {
			getDrops(entity, entity.getBlocks()).forEach(itemStack -> Block.popResource(level, entity.getOriginPos(), itemStack));
			entity.remove(Entity.RemovalReason.DISCARDED);
		}
	}

	default boolean willTreeFall(BlockPos blockPos, BlockGetter level, Player player) {
		if (getConfig().onlyFallWithRequiredTool && !getConfig().allowedToolFilter.isValid(player.getItemBySlot(EquipmentSlot.MAINHAND))) return false;

		return !(!FallingTreesConfig.getCommonConfig().disableCrouchMining &&
				player.isCrouching() != FallingTreesConfig.getClientConfig(player).invertCrouchMining);
	}

	default float fallAnimationEdgeDistance() {
		return 1;
	}

	default boolean enabled() {
		return true;
	}

	default List<ItemStack> getDrops(TreeEntity entity, Map<BlockPos, BlockState> blocks) {
		List<ItemStack> itemStacks = new ArrayList<>();
		if (entity.level instanceof ServerLevel serverLevel) {
			blocks.forEach((blockPos, blockState) -> {
				BlockEntity blockEntity = null;
				if (blockState.hasBlockEntity())

					blockEntity = serverLevel.getBlockEntity(blockPos);
				itemStacks.addAll(Block.getDrops(blockState, serverLevel, blockPos, blockEntity, entity.owner, entity.getUsedTool()));
			});
		}
		return itemStacks;
	}

	T getConfig();
}
