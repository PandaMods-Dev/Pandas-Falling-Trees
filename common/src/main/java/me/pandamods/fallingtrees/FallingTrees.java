package me.pandamods.fallingtrees;

import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.event.EventHandler;
import me.pandamods.fallingtrees.registry.EntityRegistry;
import me.pandamods.fallingtrees.registry.SoundRegistry;
import me.pandamods.fallingtrees.registry.TreeTypeRegistry;
import me.pandamods.fallingtrees.utils.BlockMapEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;

public class FallingTrees {
    public static final String MOD_ID = "fallingtrees";
	public static final FallingTreesConfig CONFIG = new FallingTreesConfig();

    public static void init() {
		TreeTypeRegistry.register();
		SoundRegistry.SOUNDS.register();
		EntityRegistry.ENTITIES.register();
		EventHandler.register();

		EntityDataSerializers.registerSerializer(BlockMapEntityData.BLOCK_MAP);
    }
}
