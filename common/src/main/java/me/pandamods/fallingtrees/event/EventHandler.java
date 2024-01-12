package me.pandamods.fallingtrees.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.utils.value.IntValue;
import me.pandamods.fallingtrees.api.TreeRegistry;
import me.pandamods.fallingtrees.api.TreeType;
import me.pandamods.fallingtrees.client.render.TreeRenderer;
import me.pandamods.fallingtrees.config.CommonConfig;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.entity.TreeEntity;
import me.pandamods.fallingtrees.registry.EntityRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.Set;

public class EventHandler {
	public static void register() {
		BlockEvent.BREAK.register(EventHandler::onBlockBreak);
	}

	private static EventResult onBlockBreak(Level level, BlockPos blockPos, BlockState blockState, ServerPlayer serverPlayer, IntValue intValue) {
		if (serverPlayer != null && makeTreeFall(blockPos, level, serverPlayer)) {
			return EventResult.interruptFalse();
		}
		return EventResult.pass();
	}

	public static boolean makeTreeFall(BlockPos blockPos, LevelAccessor level, Player player) {
		Optional<TreeType> treeTypeOptional = TreeRegistry.getTreeType(level.getBlockState(blockPos));
		return treeTypeOptional.filter(treeType -> makeTreeFall(treeType, blockPos, level, player)).isPresent();
	}

	public static boolean makeTreeFall(TreeType treeType, BlockPos blockPos, LevelAccessor level, Player player) {
		ItemStack mainItem = player.getItemBySlot(EquipmentSlot.MAINHAND);
		BlockState blockState = level.getBlockState(blockPos);
		CommonConfig commonConfig = FallingTreesConfig.getCommonConfig();

		if (!treeType.allowedToFall(player)) return false;
		if (commonConfig.limitations.treeFallRequirements.onlyRequiredTool && !treeType.allowedTool(mainItem, blockState)) return false;

		Set<BlockPos> treeBlockPos = treeType.blockGatheringAlgorithm(blockPos, level);
		if (treeBlockPos.stream().noneMatch(blockPos1 -> treeType.extraRequiredBlockCheck(level.getBlockState(blockPos1)))) return false;

		long baseAmount = treeBlockPos.stream().filter(blockPos1 -> treeType.baseBlockCheck(level.getBlockState(blockPos1))).count();
		switch (commonConfig.limitations.treeFallRequirements.maxAmountType) {
			case BLOCK_AMOUNT -> {
				if (treeBlockPos.size() > commonConfig.limitations.treeFallRequirements.maxAmount) return false;
			}
			case BASE_BLOCK_AMOUNT -> {
				if (baseAmount > commonConfig.limitations.treeFallRequirements.maxAmount) return false;
			}
		}

		if (!mainItem.isEmpty()) {
			mainItem.hurtAndBreak(commonConfig.multiplyToolDamage ? (int) baseAmount : 1, player, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		}
		player.causeFoodExhaustion(0.005f * (commonConfig.multiplyFoodExhaustion ? (int) baseAmount : 1));
		player.awardStat(Stats.BLOCK_MINED.get(blockState.getBlock()), (int) baseAmount);

		TreeEntity.destroyTree(treeBlockPos, blockPos, level, treeType, player);
		return true;
	}
}
