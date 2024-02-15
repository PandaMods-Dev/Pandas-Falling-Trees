package me.pandamods.fallingtrees.compat.fabric;

import ht.treechop.api.TreeChopEvents;
import ht.treechop.common.registry.FabricModBlocks;
import me.pandamods.fallingtrees.compat.TreeChopCompat;
import net.minecraft.world.level.block.state.BlockState;

public class TreeChopCompatImpl {
	public static void init() {
		TreeChopEvents.BEFORE_FELL.register(TreeChopCompat::beforeFellEvent);
	}

	public static boolean isCoppedLog(BlockState blockState) {
		return blockState.is(FabricModBlocks.CHOPPED_LOG);
	}
}
