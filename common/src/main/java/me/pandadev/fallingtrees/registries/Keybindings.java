package me.pandadev.fallingtrees.registries;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.FallingTreesConfig;
import net.minecraft.client.KeyMapping;

public class Keybindings {
	public static final KeyMapping SINGLE_BLOCK_MINING_TOGGLE = new KeyMapping(
		"key.fallingtrees.single_block_toggle_key",
		InputConstants.Type.KEYSYM,
		InputConstants.KEY_N,
		KeyMapping.CATEGORY_GAMEPLAY
	);

	public static void init() {
		KeyMappingRegistry.register(SINGLE_BLOCK_MINING_TOGGLE);
		ClientTickEvent.CLIENT_POST.register(minecraft -> {
				while (FallingTrees.configHolder.getConfig().one_block_mining_method.equals(FallingTreesConfig.OneBlockMiningEnum.KEYBIND_TOGGLE) &&
						SINGLE_BLOCK_MINING_TOGGLE.consumeClick()) {
					FallingTrees.configHolder.getConfig().is_mining_one_block = !FallingTrees.configHolder.getConfig().is_mining_one_block;
					FallingTrees.configHolder.save();
				}
			});
	}
}
