/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.fallingtrees.config.common.tree;

#if MC_VER <= MC_1_19_2
import net.minecraft.core.Registry;
#endif
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class StandardTreeConfig extends TreeConfig {
	public StandardTreeConfig() {
		#if MC_VER >= MC_1_20
			this.allowedToolFilter.whitelistedTags.add(ItemTags.AXES.location().toString());
		#else
			this.allowedToolFilter.whitelist.addAll(List.of(
					Registry.ITEM.getKey(Items.WOODEN_AXE).toString(),
					Registry.ITEM.getKey(Items.STONE_AXE).toString(),
					Registry.ITEM.getKey(Items.IRON_AXE).toString(),
					Registry.ITEM.getKey(Items.GOLDEN_AXE).toString(),
					Registry.ITEM.getKey(Items.DIAMOND_AXE).toString(),
					Registry.ITEM.getKey(Items.NETHERITE_AXE).toString()
			));
		#endif
	}

	public Algorithm algorithm = new Algorithm();

	public Filter logFilter = new Filter(
			List.of(BlockTags.LOGS.location().toString()),
			new ArrayList<>(),
			new ArrayList<>()
	);
	public Filter leavesFilter = new Filter(
			List.of(BlockTags.LEAVES.location().toString()),
			new ArrayList<>(),
			new ArrayList<>()
	);
	public Filter extraBlockFilter = new Filter(
			new ArrayList<>(),
			List.of(
					Blocks.VINE.arch$registryName().toString(),
					Blocks.BEE_NEST.arch$registryName().toString(),
					Blocks.COCOA.arch$registryName().toString()
			),
			new ArrayList<>()
	);

	public static class Algorithm {
		public int maxLeavesRadius = 10;
		public int maxLogAmount = 256;
		public boolean shouldFallOnMaxLogAmount = false;
		public boolean shouldIgnorePersistentLeaves = true;
	}
}
