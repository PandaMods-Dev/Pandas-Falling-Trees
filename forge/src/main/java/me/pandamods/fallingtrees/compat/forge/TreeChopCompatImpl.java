package me.pandamods.fallingtrees.compat.forge;

import ht.treechop.api.ChopEvent;
import ht.treechop.common.registry.ForgeModBlocks;
import me.pandamods.fallingtrees.compat.TreeChopCompat;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;

public class TreeChopCompatImpl {
	public static void init() {
		MinecraftForge.EVENT_BUS.addListener(TreeChopCompatImpl::beforeFellEvent);
	}

	public static void beforeFellEvent(final ChopEvent.BeforeFellEvent event) {
		if (event.getPlayer() instanceof ServerPlayer serverPlayer)
			event.setCanceled(!TreeChopCompat.beforeFellEvent(event.getLevel(), serverPlayer, event.getChoppedBlockPos(), event.getFellData()));
	}

	public static boolean isCoppedLog(BlockState blockState) {
		return blockState.is(ForgeModBlocks.CHOPPED_LOG.get());
	}
}
