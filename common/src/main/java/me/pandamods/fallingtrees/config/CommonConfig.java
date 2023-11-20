package me.pandamods.fallingtrees.config;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.config.classes.AlgorithmConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

@Config(name = FallingTrees.MOD_ID + "_common")
public class CommonConfig implements ConfigData {
	public boolean isCrouchMiningAllowed = true;
	public boolean multiplyToolDamage = true;
	public boolean multiplyFoodExhaustion = true;

	@ConfigEntry.Category("filter")
	@ConfigEntry.Gui.TransitiveObject
	public Filter filter = new Filter();

	@ConfigEntry.Category("algorithm")
	@ConfigEntry.Gui.TransitiveObject
	public AlgorithmConfig algorithm = new AlgorithmConfig();

	public static class Filter {
		@ConfigEntry.Gui.CollapsibleObject
		public FilterBlock log = new FilterBlock(List.of(BlockTags.LOGS), new ArrayList<>(), new ArrayList<>());
		@ConfigEntry.Gui.CollapsibleObject
		public FilterBlock leaves = new FilterBlock(List.of(BlockTags.LEAVES), new ArrayList<>(), new ArrayList<>());

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
				if (blacklistedBlocks.contains(block.arch$registryName()))
					return false;
				return block.defaultBlockState().getTags().anyMatch(blockTagKey -> whitelistedBlockTags.contains(blockTagKey.location().toString())) ||
						whitelistedBlocks.contains(block.arch$registryName());
			}
		}
	}
}
