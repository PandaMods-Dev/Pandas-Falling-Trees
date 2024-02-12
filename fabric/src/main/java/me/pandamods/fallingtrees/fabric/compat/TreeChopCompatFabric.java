package me.pandamods.fallingtrees.fabric.compat;

import ht.treechop.api.TreeChopEvents;
import me.pandamods.fallingtrees.compat.TreeChopCompat;

public class TreeChopCompatFabric {
	public static void init() {
		TreeChopEvents.BEFORE_FELL.register(TreeChopCompat::beforeFellEvent);
	}
}
