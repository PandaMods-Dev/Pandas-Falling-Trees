package me.pandamods.fallingtrees.config.common.tree;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class VerticalTreeConfig extends TreeConfig {
	public Filter filter = new Filter(
			new ArrayList<>(),
			List.of(Registry.BLOCK.getKey(Blocks.CACTUS).toString(), Registry.BLOCK.getKey(Blocks.BAMBOO).toString()),
			new ArrayList<>()
	);
}
