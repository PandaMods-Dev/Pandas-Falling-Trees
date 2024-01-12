package me.pandamods.fallingtrees;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import me.pandamods.fallingtrees.client.render.TreeRenderer;
import me.pandamods.fallingtrees.registry.EntityRegistry;

public class FallingTreesClient {
    public static void init() {
		EntityRendererRegistry.register(EntityRegistry.TREE, TreeRenderer::new);
    }
}
