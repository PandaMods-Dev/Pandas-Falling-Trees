package me.pandadev.fallingtrees;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@Config(name = FallingTrees.MOD_ID)
public class FallingTreesConfig implements ConfigData {
//	@ConfigEntry.Category("common")
	public boolean allow_one_block_mining = true;

//	@ConfigEntry.Category("common")
	public List<String> whitelisted_log_blocks = List.of("minecraft:mushroom_stem");
//	@ConfigEntry.Category("common")
	public List<String> whitelisted_log_block_tags = List.of("minecraft:logs");

//	@ConfigEntry.Category("common")
	public List<String> whitelisted_leaves_blocks = List.of("minecraft:red_mushroom_block", "minecraft:brown_mushroom_block");
//	@ConfigEntry.Category("common")
	public List<String> whitelisted_leaves_block_tags = List.of("minecraft:leaves", "minecraft:wart_blocks");

//	@ConfigEntry.Category("common")
	public List<String> blacklisted_log_blocks = new ArrayList<>();
//	@ConfigEntry.Category("common")
	public List<String> blacklisted_leaves_blocks = new ArrayList<>();

//	@ConfigEntry.Category("client")
//	public OneBlockMiningEnum one_block_mining_method = OneBlockMiningEnum.CROUCH;

	public enum OneBlockMiningEnum {
		CROUCH,
		KEYBIND_TOGGLE,
		OFF
	}
}
