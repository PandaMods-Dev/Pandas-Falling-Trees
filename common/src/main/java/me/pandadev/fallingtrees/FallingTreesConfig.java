package me.pandadev.fallingtrees;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@Config(name = FallingTrees.MOD_ID)
public class FallingTreesConfig implements ConfigData {
	@ConfigEntry.Category("common")
	public boolean allow_one_block_mining = true;

	@ConfigEntry.Category("common")
	public List<String> whitelisted_log_blocks = List.of("minecraft:mushroom_stem");
	@ConfigEntry.Category("common")
	public List<String> whitelisted_log_block_tags = List.of("minecraft:logs");

	@ConfigEntry.Category("common")
	public List<String> whitelisted_leaves_blocks = List.of("minecraft:red_mushroom_block", "minecraft:brown_mushroom_block");
	@ConfigEntry.Category("common")
	public List<String> whitelisted_leaves_block_tags = List.of("minecraft:leaves", "minecraft:wart_blocks");

	@ConfigEntry.Category("common")
	public List<String> blacklisted_log_blocks = new ArrayList<>();
	@ConfigEntry.Category("common")
	public List<String> blacklisted_leaves_blocks = new ArrayList<>();

	@ConfigEntry.Category("common")
	public float food_exhaustion_multiplier = 1f;
	@ConfigEntry.Category("common")
	public float item_damage_multiplier = 1f;

	@ConfigEntry.Category("common")
	public boolean tree_mining_speed_by_log_amount = true;
	@ConfigEntry.Category("common")
	public float tree_mining_speed_multiplier = 1f;

	@ConfigEntry.Category("common")
	@ConfigEntry.Gui.CollapsibleObject
	public TreeLimit tree_limit = new TreeLimit();

	@ConfigEntry.Category("common")
	public boolean sound_effect = true;
	@ConfigEntry.Category("common")
	public float sound_effect_volume = 1;

	@ConfigEntry.Category("client")
	public OneBlockMiningEnum one_block_mining_method = OneBlockMiningEnum.CROUCH;

	@ConfigEntry.Category("client")
	@ConfigEntry.Gui.Tooltip
	public boolean is_mining_one_block = false;

	public enum OneBlockMiningEnum {
		CROUCH,
		KEYBIND_TOGGLE,
		OFF
	}

	public static class TreeLimit {
		@ConfigEntry.Gui.Tooltip
		public int tree_size_limit = 200;

		@ConfigEntry.Gui.Tooltip
		public LimitMethodEnum tree_limit_method = LimitMethodEnum.LOGS;

		public boolean only_fall_on_tool_use = false;

		public enum LimitMethodEnum {
			BLOCKS,
			LOGS
		}
	}
}
