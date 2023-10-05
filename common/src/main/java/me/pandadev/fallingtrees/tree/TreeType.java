package me.pandadev.fallingtrees.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;

public abstract class TreeType {
	public abstract boolean blockChecker(BlockPos pos, BlockGetter level);
	public abstract List<BlockPos> blockDetectionAlgorithm(BlockPos pos, BlockGetter level);

	public boolean extraBlockRequirement(Map<BlockPos, BlockState> blocks, BlockGetter level) {
		return true;
	}

	public void onFall(Player player, BlockPos pos, Level level) {

	}
}
