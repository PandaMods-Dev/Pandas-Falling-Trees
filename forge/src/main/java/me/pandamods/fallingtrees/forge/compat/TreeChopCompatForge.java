package me.pandamods.fallingtrees.forge.compat;

import ht.treechop.api.ChopEvent;
import ht.treechop.common.chop.ChopUtil;
import me.pandamods.fallingtrees.compat.TreeChopCompat;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class TreeChopCompatForge {
	public static void init() {
		MinecraftForge.EVENT_BUS.addListener(TreeChopCompatForge::beforeFellEvent);
	}

	public static void beforeFellEvent(final ChopEvent.BeforeFellEvent event) {
		if (event.getPlayer() instanceof ServerPlayer serverPlayer)
			event.setCanceled(!TreeChopCompat.beforeFellEvent(event.getLevel(), serverPlayer, event.getChoppedBlockPos(), event.getFellData()));
	}
}
