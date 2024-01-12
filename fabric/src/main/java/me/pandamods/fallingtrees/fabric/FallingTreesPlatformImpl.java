package me.pandamods.fallingtrees.fabric;

import me.pandamods.fallingtrees.FallingTreesPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FallingTreesPlatformImpl {
    /**
     * This is our actual method to {@link FallingTreesPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
