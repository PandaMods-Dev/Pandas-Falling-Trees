package me.pandadev.fallingtrees.fabric;

import me.pandadev.fallingtrees.FallingTrees;
import net.fabricmc.api.ModInitializer;

public class FallingTreesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FallingTrees.init();
    }
}