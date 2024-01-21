package me.pandamods.fallingtrees.api;

import net.minecraft.core.BlockPos;

import java.util.Set;

public record TreeData(
		Set<BlockPos> blocks,
		float miningSpeedMultiply,
		boolean shouldFall
) {
}
