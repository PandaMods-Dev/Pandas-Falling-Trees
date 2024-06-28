package me.pandamods.fallingtrees.compat;

import dev.architectury.injectables.annotations.ExpectPlatform;
import ht.treechop.api.ChopDataImmutable;
import ht.treechop.api.FellData;
import ht.treechop.common.chop.ChopUtil;
import me.pandamods.fallingtrees.event.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

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
