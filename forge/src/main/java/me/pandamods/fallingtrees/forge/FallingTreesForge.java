package me.pandamods.fallingtrees.forge;

import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FallingTrees.MOD_ID)
public class FallingTreesForge {
    public FallingTreesForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(FallingTrees.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        FallingTrees.init();
    }
}