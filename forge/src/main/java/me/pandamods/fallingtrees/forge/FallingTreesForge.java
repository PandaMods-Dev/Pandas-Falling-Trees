package me.pandamods.fallingtrees.forge;

import dev.architectury.platform.forge.EventBuses;
import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.FallingTreesClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FallingTrees.MOD_ID)
public class FallingTreesForge {
    public FallingTreesForge() {
        EventBuses.registerModEventBus(FallingTrees.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        FallingTrees.init();

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> FallingTreesForgeClient::clientInit);
    }
}
