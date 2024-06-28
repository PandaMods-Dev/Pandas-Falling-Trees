package me.pandamods.fallingtrees.compat;

import dev.architectury.platform.Platform;

public class Compat {
	public static boolean hasTreeChop() {
		return Platform.isModLoaded("treechop");
	}
}
