package me.pandadev.fallingtrees.registries;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.network.ChangeMiningModePacket;
import me.pandadev.fallingtrees.utils.PlayerExtension;
import net.minecraft.client.KeyMapping;

public class Keybindings {
	public static final KeyMapping SINGLE_BLOCK_MINING_KEY = new KeyMapping(
		"key.fallingtrees.single_block_toggle_key",
		InputConstants.Type.KEYSYM,
		InputConstants.KEY_N,
		KeyMapping.CATEGORY_GAMEPLAY
	);

	private static boolean miningModeHold = false;

	public static void init() {
		KeyMappingRegistry.register(SINGLE_BLOCK_MINING_KEY);

		ClientTickEvent.CLIENT_POST.register(instance -> {
			if (instance.player != null) {
				switch (FallingTrees.getClientConfig().one_block_mining_method) {
					case KEYBIND_TOGGLE -> {
						while (SINGLE_BLOCK_MINING_KEY.consumeClick()) {
							ChangeMiningModePacket.sendToServer(!((PlayerExtension) instance.player).isMiningOneBlock());
						}
					}
					case KEYBIND_HOLD -> {
						if (miningModeHold != SINGLE_BLOCK_MINING_KEY.isDown()) {
							miningModeHold = SINGLE_BLOCK_MINING_KEY.isDown();
							ChangeMiningModePacket.sendToServer(miningModeHold);
						}
					}
					case SHIFT -> {
						if (miningModeHold != instance.options.keyShift.isDown()) {
							miningModeHold = instance.options.keyShift.isDown();
							ChangeMiningModePacket.sendToServer(miningModeHold);
						}
					}
				}
			}
		});
	}
}
