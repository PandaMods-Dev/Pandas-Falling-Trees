package me.pandamods.fallingtrees;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.logging.LogUtils;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import me.pandamods.fallingtrees.api.TreeData;
import me.pandamods.fallingtrees.api.TreeDataBuilder;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.event.EventHandler;
import me.pandamods.fallingtrees.registry.EntityRegistry;
import me.pandamods.fallingtrees.registry.SoundRegistry;
import me.pandamods.fallingtrees.registry.TreeTypeRegistry;
import me.pandamods.fallingtrees.utils.BlockMapEntityData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
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

		if (#if MC_VER >= MC_1_20_5 !Platform.isNeoForge() #else true #endif)
			EntityDataSerializers.registerSerializer(BlockMapEntityData.BLOCK_MAP);
    }

	public static ResourceLocation ID(String path) {
		#if MC_VER >= MC_1_21
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
		#else
			return new ResourceLocation(MOD_ID, path);
		#endif
	}
}
