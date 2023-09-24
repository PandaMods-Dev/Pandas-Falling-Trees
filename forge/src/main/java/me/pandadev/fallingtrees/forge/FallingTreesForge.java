package me.pandadev.fallingtrees.forge;

import dev.architectury.platform.forge.EventBuses;
import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FallingTrees.MOD_ID)
public class FallingTreesForge {
    public FallingTreesForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(FallingTrees.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
            FallingTrees.init();

		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
				new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) ->
						AutoConfig.getConfigScreen(ModConfig.class, screen).get()));
    }
}