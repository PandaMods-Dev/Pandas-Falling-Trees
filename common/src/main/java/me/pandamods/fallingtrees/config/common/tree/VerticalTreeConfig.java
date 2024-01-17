package me.pandamods.fallingtrees.config.common.tree;

import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class VerticalTreeConfig extends TreeConfig {
	public Filter filter = new Filter(
			new ArrayList<>(),
			List.of(Blocks.CACTUS, Blocks.BAMBOO),
			new ArrayList<>()
	);
}
