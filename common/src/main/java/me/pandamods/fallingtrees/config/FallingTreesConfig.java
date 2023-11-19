package me.pandamods.fallingtrees.config;

import dev.architectury.platform.Platform;
import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.event.EventHandler;
import me.pandamods.fallingtrees.network.ConfigPacket;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;

public class FallingTreesConfig {
	public final ConfigHolder<ModConfig> configHolder;
	private CommonConfig commonConfig;

	public FallingTreesConfig() {
		configHolder = AutoConfig.register(ModConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));

		if (Platform.getEnv().equals(EnvType.CLIENT)) {
			configHolder.registerSaveListener(this::saveConfig);
		}
	}

	private InteractionResult saveConfig(ConfigHolder<ModConfig> modConfigConfigHolder, ModConfig modConfig) {
		if (Minecraft.getInstance().level != null) {
			ConfigPacket.sendToServer();
		}

		return InteractionResult.PASS;
	}

	public static ClientConfig getClientConfig() {
		return FallingTrees.CONFIG.configHolder.getConfig().client;
	}

	public static CommonConfig getCommonConfig() {
		if (FallingTrees.CONFIG.commonConfig != null)
			return FallingTrees.CONFIG.commonConfig;
		return FallingTrees.CONFIG.configHolder.getConfig().common;
	}

	public void setCommonConfig(CommonConfig commonConfig) {
		this.commonConfig = commonConfig;
	}
}
