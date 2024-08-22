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

#if MC_VER >= MC_1_20
import net.minecraft.core.registries.BuiltInRegistries;
#else
import net.minecraft.core.Registry;
#endif
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class TreeConfig {
	public boolean enabled = true;

	public boolean onlyFallWithRequiredTool = false;
	public Filter allowedToolFilter = new Filter(
			new ArrayList<>(),
			new ArrayList<>(),
			new ArrayList<>()
	);

	public static class Filter {
		public List<String> whitelistedTags;
		public List<String> whitelist;
		public List<String> blacklist;

		public Filter(List<String> whitelistedBlockTags, List<String> whitelistedBlocks, List<String> blacklistedBlocks) {
			this.whitelistedTags = whitelistedBlockTags;
			this.whitelist = whitelistedBlocks;
			this.blacklist = blacklistedBlocks;
		}

		public boolean isValid(BlockState blockState) {
			Block block = blockState.getBlock();
			#if MC_VER >= MC_1_20
				ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
			#else
				ResourceLocation resourceLocation = Registry.BLOCK.getKey(block);
			#endif
			if (blacklist.contains(resourceLocation.toString()))
				return false;
			return blockState.getTags().anyMatch(blockTagKey -> whitelistedTags.contains(blockTagKey.location().toString())) ||
					whitelist.contains(resourceLocation.toString());
		}

		public boolean isValid(ItemStack itemStack) {
			Item item = itemStack.getItem();
			#if MC_VER >= MC_1_20
				ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(item);
			#else
				ResourceLocation resourceLocation = Registry.ITEM.getKey(item);
			#endif
			if (blacklist.contains(resourceLocation.toString()))
				return false;
			return itemStack.getTags().anyMatch(blockTagKey -> whitelistedTags.contains(blockTagKey.location().toString())) ||
					whitelist.contains(resourceLocation.toString());
		}
	}
}
