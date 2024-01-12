package me.pandamods.fallingtrees.forge;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class FallingTreesPlatformImpl {
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
