package me.pandadev.fallingtrees.fabric;

import me.pandadev.fallingtrees.FallingTrees;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class FallingTreesFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        FallingTrees.init();
    }

	@Override
	public void onInitializeClient() {
		FallingTrees.clientInit();
	}
}