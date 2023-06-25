package me.pandadev.dynamictrees.fabric;

import me.pandadev.dynamictrees.DynamicTrees;
import net.fabricmc.api.ModInitializer;

public class DynamicTreesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DynamicTrees.init();
    }
}