package me.pandamods.fallingtrees;

import me.pandamods.fallingtrees.config.ClientConfig;
import me.pandamods.fallingtrees.config.CommonConfig;
import me.pandamods.fallingtrees.config.ModConfig;
import me.pandamods.fallingtrees.event.EventHandler;
import me.pandamods.fallingtrees.network.PacketHandler;
import me.pandamods.fallingtrees.registry.EntityRegistry;
import me.pandamods.fallingtrees.registry.TreeTypeRegistry;
import me.pandamods.fallingtrees.utils.BlockMapEntityData;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public class FallingTrees {
	public static final String MOD_ID = "fallingtrees";
	private static ConfigHolder<ModConfig> configHolder;
	private static CommonConfig commonConfig;


	public static void init() {
		configHolder = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

		TreeTypeRegistry.register();
		EntityRegistry.ENTITIES.register();
		PacketHandler.register();
		EventHandler.register();

		EntityDataSerializers.registerSerializer(BlockMapEntityData.BLOCK_MAP);
	}

	public static CommonConfig getCommonConfig() {
		if (FallingTrees.commonConfig != null)
			return FallingTrees.commonConfig;
		return configHolder.getConfig().common;
	}

	public static void setCommonConfig(CommonConfig commonConfig) {
		FallingTrees.commonConfig = commonConfig;
	}

	public static ClientConfig getClientConfig() {
		return configHolder.getConfig().client;
	}

	public static ConfigHolder<ModConfig> getConfigHolder() {
		return configHolder;
	}
}
