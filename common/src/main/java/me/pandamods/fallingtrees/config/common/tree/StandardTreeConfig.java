package me.pandamods.fallingtrees.config.common.tree;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class StandardTreeConfig extends TreeConfig {
	public int maxLeavesRadius = 10;

	public Filter logFilter = new Filter(
			List.of(BlockTags.LOGS),
			List.of(Blocks.MUSHROOM_STEM),
			new ArrayList<>()
	);
	public Filter leavesFilter = new Filter(
			List.of(BlockTags.LEAVES),
			List.of(Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK),
			new ArrayList<>()
	);
}
