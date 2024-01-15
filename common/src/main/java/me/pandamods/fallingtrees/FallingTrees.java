package me.pandamods.fallingtrees;

import com.mojang.logging.LogUtils;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.event.EventHandler;
import me.pandamods.fallingtrees.registry.EntityRegistry;
import me.pandamods.fallingtrees.registry.SoundRegistry;
import me.pandamods.fallingtrees.registry.TreeTypeRegistry;
import me.pandamods.fallingtrees.utils.BlockMapEntityData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.syncher.EntityDataSerializers;
import org.slf4j.Logger;

public class FallingTrees {
    public static final String MOD_ID = "fallingtrees";
	public static final FallingTreesConfig CONFIG = new FallingTreesConfig();
	public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
		TreeTypeRegistry.register();
		SoundRegistry.SOUNDS.register();
		EntityRegistry.ENTITIES.register();
		EventHandler.register();

		EntityDataSerializers.registerSerializer(BlockMapEntityData.BLOCK_MAP);
    }
}
