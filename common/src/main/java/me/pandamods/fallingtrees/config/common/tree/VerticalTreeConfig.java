package me.pandamods.fallingtrees.config.common.tree;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class VerticalTreeConfig extends TreeConfig {
	public Filter filter = new Filter(
			new ArrayList<>(),
			List.of(BuiltInRegistries.BLOCK.getKey(Blocks.CACTUS).toString(), BuiltInRegistries.BLOCK.getKey(Blocks.BAMBOO).toString()),
			new ArrayList<>()
	);
}
