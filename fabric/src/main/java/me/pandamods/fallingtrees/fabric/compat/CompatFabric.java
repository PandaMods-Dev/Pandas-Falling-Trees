package me.pandamods.fallingtrees.fabric.compat;

import dev.architectury.platform.Platform;
import me.pandamods.fallingtrees.compat.Compat;

public class CompatFabric {
	public static void init() {
		if (Compat.hasTreeChop()) TreeChopCompatFabric.init();
	}
}
