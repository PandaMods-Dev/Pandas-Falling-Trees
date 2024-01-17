package me.pandamods.fallingtrees.config.common.tree;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TreeConfig {
	public boolean enabled = true;

	public static class Filter {
		public List<String> whitelistedTags;
		public List<String> whitelist;
		public List<String> blacklist;

		public Filter(List<TagKey<Block>> whitelistedBlockTags, List<Block> whitelistedBlocks, List<Block> blacklistedBlocks) {
			this.whitelistedTags = whitelistedBlockTags.stream().map(blockTagKey -> blockTagKey.location().toString()).toList();
			this.whitelist = whitelistedBlocks.stream().map(block -> block.arch$registryName().toString()).toList();
			this.blacklist = blacklistedBlocks.stream().map(block -> block.arch$registryName().toString()).toList();
		}

		public boolean isValid(BlockState blockState) {
			Block block = blockState.getBlock();
			ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
			if (blacklist.contains(resourceLocation.toString()))
				return false;
			return blockState.getTags().anyMatch(blockTagKey -> whitelistedTags.contains(blockTagKey.location().toString())) ||
					whitelist.contains(resourceLocation.toString());
		}
	}
}
