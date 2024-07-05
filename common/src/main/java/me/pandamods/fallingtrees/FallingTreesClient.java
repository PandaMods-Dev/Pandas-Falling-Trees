package me.pandamods.fallingtrees;

import dev.architectury.platform.Platform;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import me.pandamods.fallingtrees.client.render.TreeRenderer;
import me.pandamods.fallingtrees.registry.EntityRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FallingTreesClient {
    public static void init() {
		if (#if MC_VER >= MC_1_20_5 !Platform.isNeoForge() #else true #endif) {
			EntityRendererRegistry.register(EntityRegistry.TREE, TreeRenderer::new);
		}
    }
}
