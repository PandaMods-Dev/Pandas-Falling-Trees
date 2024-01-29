package me.pandamods.fallingtrees.api;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.Set;

public record TreeData(
		Set<BlockPos> blocks,
		float miningSpeedMultiply,
		boolean shouldFall
) {
}
