package me.pandamods.fallingtrees.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.utils.value.IntValue;
import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.api.TreeRegistry;
import me.pandamods.fallingtrees.api.TreeType;
import me.pandamods.fallingtrees.client.render.TreeRenderer;
import me.pandamods.fallingtrees.config.CommonConfig;
import me.pandamods.fallingtrees.entity.TreeEntity;
import me.pandamods.fallingtrees.network.ConfigPacket;
import me.pandamods.fallingtrees.registry.EntityRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class EventHandler {
	public static void register() {
		if (Platform.getEnv() == EnvType.CLIENT) {
			if (Platform.isFabric()) ClientLifecycleEvent.CLIENT_SETUP.register(EventHandler::onClientSetup);
			if (Platform.isForge()) onClientSetup(Minecraft.getInstance());
		}
		BlockEvent.BREAK.register(EventHandler::onBlockBreak);
		PlayerEvent.PLAYER_JOIN.register(EventHandler::onPlayerJoin);
	}

	private static void onClientSetup(Minecraft minecraft) {
		ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(EventHandler::onClientPlayerJoin);

		EntityRendererRegistry.register(EntityRegistry.TREE, TreeRenderer::new);
	}

	private static EventResult onBlockBreak(Level level, BlockPos blockPos, BlockState blockState, ServerPlayer serverPlayer, IntValue intValue) {
		Optional<TreeType> treeTypeOptional = TreeRegistry.getTreeType(blockState);
		if (treeTypeOptional.isPresent()) {
			if (makeTreeFall(treeTypeOptional.get(), blockPos, level, serverPlayer)) {
				return EventResult.interruptFalse();
			}
		}
		return EventResult.pass();
	}

	private static void onPlayerJoin(ServerPlayer serverPlayer) {
		ConfigPacket.sendToPlayer(serverPlayer);
	}
	private static void onClientPlayerJoin(LocalPlayer localPlayer) {
		ConfigPacket.sendToServer();
	}

	public static boolean makeTreeFall(BlockPos blockPos, LevelAccessor level, Player player) {
		Optional<TreeType> treeTypeOptional = TreeRegistry.getTreeType(level.getBlockState(blockPos));
		return treeTypeOptional.filter(treeType -> makeTreeFall(treeType, blockPos, level, player)).isPresent();
	}

	public static boolean makeTreeFall(TreeType treeType, BlockPos blockPos, LevelAccessor level, Player player) {
		ItemStack mainItem = player.getItemBySlot(EquipmentSlot.MAINHAND);
		BlockState blockState = level.getBlockState(blockPos);
		CommonConfig commonConfig = FallingTrees.getCommonConfig();

		if (commonConfig.isCrouchMiningAllowed &&
				player.isCrouching() != ConfigPacket.getClientConfig(player).getBoolean("invertCrouchMining")) return false;
		if (commonConfig.limit.onlyRequiredTool && !treeType.allowedTool(mainItem, blockState)) return false;

		Set<BlockPos> treeBlockPos = treeType.blockGatheringAlgorithm(blockPos, level);
		if (treeBlockPos.stream().noneMatch(blockPos1 -> treeType.extraRequiredBlockCheck(level.getBlockState(blockPos1)))) return false;

		long baseAmount = treeBlockPos.stream().filter(blockPos1 -> treeType.baseBlockCheck(level.getBlockState(blockPos1))).count();
		switch (commonConfig.limit.limitType) {
			case BLOCK_AMOUNT -> {
				if (treeBlockPos.size() > commonConfig.limit.blockAmountLimit) return false;
			}
			case BASE_BLOCK_AMOUNT -> {
				if (baseAmount > commonConfig.limit.blockAmountLimit) return false;
			}
		}

		if (!mainItem.isEmpty()) {
			mainItem.hurtAndBreak(commonConfig.damageUsedTool ? (int) baseAmount : 1, player, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		}
		player.causeFoodExhaustion(0.005f * (commonConfig.causeFoodExhaustion ? (int) baseAmount : 1));
		player.awardStat(Stats.BLOCK_MINED.get(blockState.getBlock()), (int) baseAmount);

		TreeEntity.destroyTree(treeBlockPos, blockPos, level, treeType, player);
		return true;
	}
}
