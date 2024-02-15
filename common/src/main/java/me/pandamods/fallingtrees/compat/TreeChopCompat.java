package me.pandamods.fallingtrees.compat;

import dev.architectury.injectables.annotations.ExpectPlatform;
import ht.treechop.api.FellData;
import ht.treechop.api.TreeChopEvents;
import ht.treechop.api.TreeData;
import ht.treechop.common.chop.ChopUtil;
import me.pandamods.fallingtrees.api.Tree;
import me.pandamods.fallingtrees.api.TreeDataBuilder;
import me.pandamods.fallingtrees.api.TreeRegistry;
import me.pandamods.fallingtrees.entity.TreeEntity;
import me.pandamods.fallingtrees.event.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TreeChopCompat {
	public static boolean beforeFellEvent(Level level, ServerPlayer serverPlayer, BlockPos blockPos, FellData fellData) {
		return tryMakeTreeFall(blockPos, level, serverPlayer);
	}

	public static boolean tryMakeTreeFall(BlockPos blockPos, LevelAccessor level, ServerPlayer player) {
		if (isCoppedLog(level.getBlockState(blockPos)))
			return tryMakeTreeFall(blockPos.above(), level, player);
		return EventHandler.makeTreeFall(blockPos.above(), level, player);
	}

	public static boolean isChoppable(Level level, BlockPos blockPos) {
		if (Compat.hasTreeChop()) {
			return ChopUtil.isBlockChoppable(level, blockPos);
		}
		return false;
	}

	@ExpectPlatform
	public static boolean isCoppedLog(BlockState blockState) {
		throw new AssertionError();
	}
}
