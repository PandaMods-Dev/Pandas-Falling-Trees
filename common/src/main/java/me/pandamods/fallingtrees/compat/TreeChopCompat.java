package me.pandamods.fallingtrees.compat;

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
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TreeChopCompat {
	public static boolean beforeFellEvent(Level level, ServerPlayer serverPlayer, BlockPos blockPos, FellData fellData) {
		return EventHandler.makeTreeFall(blockPos.above(), level, serverPlayer);
	}

	public static boolean isChoppable(Level level, BlockPos blockPos) {
		if (Compat.hasTreeChop()) {
			return ChopUtil.isBlockChoppable(level, blockPos);
		}
		return false;
	}
}
