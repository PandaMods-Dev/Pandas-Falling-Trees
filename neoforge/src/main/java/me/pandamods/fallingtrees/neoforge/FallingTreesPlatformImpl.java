package me.pandamods.fallingtrees.neoforge;


import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class FallingTreesPlatformImpl {
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
