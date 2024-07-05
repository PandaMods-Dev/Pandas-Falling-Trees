package me.pandamods.fallingtrees.compat.forge;

import me.pandamods.fallingtrees.compat.Compat;

public class CompatForge {
	public static void init() {
		if (Compat.hasTreeChop()) TreeChopCompatImpl.init();
	}
}
