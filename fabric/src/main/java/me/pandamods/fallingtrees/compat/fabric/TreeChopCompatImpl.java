/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.fallingtrees.compat.fabric;

#if MC_VER <= MC_1_20
import ht.treechop.api.TreeChopEvents;
import ht.treechop.common.registry.FabricModBlocks;
#endif
import me.pandamods.fallingtrees.compat.TreeChopCompat;
import net.minecraft.world.level.block.state.BlockState;

public class TreeChopCompatImpl {
	public static void init() {
		#if MC_VER <= MC_1_20
		TreeChopEvents.BEFORE_FELL.register(TreeChopCompat::beforeFellEvent);
		#endif
	}

	public static boolean isCoppedLog(BlockState blockState) {
		#if MC_VER <= MC_1_20
			return blockState.is(FabricModBlocks.CHOPPED_LOG);
		#else
			return false;
		#endif
	}
}
