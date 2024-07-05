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
