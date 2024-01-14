package me.pandamods.fallingtrees.config.common;

import dev.architectury.extensions.injected.InjectedRegistryEntryExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class FilterConfig {
	public FilterBlock log = new FilterBlock(
			List.of(BlockTags.LOGS),
			List.of(Blocks.MUSHROOM_STEM),
			new ArrayList<>()
	);
	public FilterBlock leaves = new FilterBlock(
			List.of(BlockTags.LEAVES),
			List.of(Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK),
			new ArrayList<>()
	);
	public FilterBlock decorationBlocks = new FilterBlock(
			new ArrayList<>(),
			List.of(Blocks.VINE, Blocks.BEE_NEST),
			new ArrayList<>()
	);

	public static class FilterBlock {
		public List<String> whitelistedBlockTags;
		public List<String> whitelistedBlocks;
		public List<String> blacklistedBlocks;

		public FilterBlock(List<TagKey<Block>> whitelistedBlockTags, List<Block> whitelistedBlocks, List<Block> blacklistedBlocks) {
			this.whitelistedBlockTags = whitelistedBlockTags.stream().map(blockTagKey -> blockTagKey.location().toString()).toList();
			this.whitelistedBlocks = whitelistedBlocks.stream().map(block -> block.arch$registryName().toString()).toList();
			this.blacklistedBlocks = blacklistedBlocks.stream().map(block -> block.arch$registryName().toString()).toList();
		}

		public boolean isValid(Block block) {
			if (blacklistedBlocks.contains(block.arch$registryName().toString()))
				return false;
			return block.defaultBlockState().getTags().anyMatch(blockTagKey -> whitelistedBlockTags.contains(blockTagKey.location().toString())) ||
					whitelistedBlocks.contains(block.arch$registryName().toString());
		}
	}
}
