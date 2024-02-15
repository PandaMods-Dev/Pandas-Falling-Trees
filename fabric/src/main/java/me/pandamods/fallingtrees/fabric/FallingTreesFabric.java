package me.pandamods.fallingtrees.fabric;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.compat.fabric.CompatFabric;
import net.fabricmc.api.ModInitializer;

public class FallingTreesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FallingTrees.init();
		CompatFabric.init();
    }
}
