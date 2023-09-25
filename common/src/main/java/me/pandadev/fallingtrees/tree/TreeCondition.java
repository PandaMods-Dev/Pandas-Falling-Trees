package me.pandadev.fallingtrees.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public interface TreeCondition {
	boolean condition(List<BlockPos> blocks, Level level);
}
