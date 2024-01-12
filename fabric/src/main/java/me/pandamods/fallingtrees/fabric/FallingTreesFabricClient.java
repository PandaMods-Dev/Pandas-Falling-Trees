package me.pandamods.fallingtrees.fabric;

import me.pandamods.fallingtrees.FallingTreesClient;
import net.fabricmc.api.ClientModInitializer;

public class FallingTreesFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FallingTreesClient.init();
    }
}
