package me.pandadev.dynamictrees.forge;

import dev.architectury.platform.forge.EventBuses;
import me.pandadev.dynamictrees.DynamicTrees;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DynamicTrees.MOD_ID)
public class DynamicTreesForge {
    public DynamicTreesForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(DynamicTrees.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
		DynamicTrees.init();
    }
}