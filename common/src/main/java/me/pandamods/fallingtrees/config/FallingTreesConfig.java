package me.pandamods.fallingtrees.config;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.api.config.holders.CommonConfigHolder;
import net.minecraft.world.entity.player.Player;

public class FallingTreesConfig {
	public final ClientConfigHolder<ClientConfig> clientConfigHolder = PandaLibConfig.registerClient(ClientConfig.class);
	public final CommonConfigHolder<CommonConfig> commonConfigHolder = PandaLibConfig.registerCommon(CommonConfig.class);

	public static ClientConfig getClientConfig(Player player) {
		return FallingTrees.CONFIG.clientConfigHolder.getConfig(player);
	}

	public static ClientConfig getClientConfig() {
		return FallingTrees.CONFIG.clientConfigHolder.get();
	}

	public static CommonConfig getCommonConfig() {
		return FallingTrees.CONFIG.commonConfigHolder.get();
	}
}
