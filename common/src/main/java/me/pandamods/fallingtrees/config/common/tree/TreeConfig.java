package me.pandamods.fallingtrees.config.common.tree;

import net.minecraft.core.Registry;
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
			ResourceLocation resourceLocation = Registry.BLOCK.getKey(block);
			if (blacklist.contains(resourceLocation.toString()))
				return false;
			return blockState.getTags().anyMatch(blockTagKey -> whitelistedTags.contains(blockTagKey.location().toString())) ||
					whitelist.contains(resourceLocation.toString());
		}

		public boolean isValid(ItemStack itemStack) {
			Item item = itemStack.getItem();
			ResourceLocation resourceLocation = Registry.ITEM.getKey(item);
			if (blacklist.contains(resourceLocation.toString()))
				return false;
			return itemStack.getTags().anyMatch(blockTagKey -> whitelistedTags.contains(blockTagKey.location().toString())) ||
					whitelist.contains(resourceLocation.toString());
		}
	}
}
