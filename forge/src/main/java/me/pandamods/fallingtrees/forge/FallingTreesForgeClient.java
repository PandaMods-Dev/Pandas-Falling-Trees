package me.pandamods.fallingtrees.forge;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.FallingTreesClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FallingTrees.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FallingTreesForgeClient {
	@SubscribeEvent
	public static void clientInit(FMLClientSetupEvent event) {
		FallingTreesClient.init();
	}
}
