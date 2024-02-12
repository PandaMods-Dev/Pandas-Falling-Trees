package me.pandamods.fallingtrees.forge.compat;

import dev.architectury.platform.Platform;
import me.pandamods.fallingtrees.compat.Compat;
import net.minecraftforge.eventbus.api.IEventBus;

public class CompatForge {
	public static void init() {
		if (Compat.hasTreeChop()) TreeChopCompatForge.init();
	}
}
