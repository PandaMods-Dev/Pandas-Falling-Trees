package me.pandamods.fallingtrees.config.common.tree;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class MushroomTreeConfig extends TreeConfig {
	public Filter stemFilter = new Filter(
			new ArrayList<>(),
			List.of(Blocks.MUSHROOM_STEM.arch$registryName().toString()),
			new ArrayList<>()
	);
	public Filter capFilter = new Filter(
			new ArrayList<>(),
			List.of(Blocks.RED_MUSHROOM_BLOCK.arch$registryName().toString(), Blocks.BROWN_MUSHROOM_BLOCK.arch$registryName().toString()),
			new ArrayList<>()
	);
}
