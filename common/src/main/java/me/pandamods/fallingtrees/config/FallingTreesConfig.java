package me.pandamods.fallingtrees.config;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.pandalib.config.api.ConfigRegistry;
import me.pandamods.pandalib.config.api.holders.ClientConfigHolder;
import me.pandamods.pandalib.config.api.holders.CommonConfigHolder;
import net.minecraft.world.entity.player.Player;

public class FallingTreesConfig {
	public final ClientConfigHolder<ClientConfig> clientConfigHolder = ConfigRegistry.registerClient(ClientConfig.class);
	public final CommonConfigHolder<CommonConfig> commonConfigHolder = ConfigRegistry.registerCommon(CommonConfig.class);

	public FallingTreesConfig() {
	}

	public static ClientConfig getClientConfig(Player player) {
		return FallingTrees.CONFIG.clientConfigHolder.getClient(player);
	}

	public static ClientConfig getClientConfig() {
		return FallingTrees.CONFIG.clientConfigHolder.get();
	}

	public static CommonConfig getCommonConfig() {
		return FallingTrees.CONFIG.commonConfigHolder.get();
	}
}
