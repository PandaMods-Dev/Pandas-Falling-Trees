package me.pandamods.fallingtrees.forge;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.FallingTreesClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class FallingTreesForgeClient {
	public static void clientInit() {
		FallingTreesClient.init();
//		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
//				new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen)));
	}
}
