package me.pandadev.fallingtrees.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@Config(name = "server")
public class ServerConfig implements ConfigData {
	public boolean allow_one_block_mining = true;

	public List<String> whitelisted_log_blocks = List.of("minecraft:mushroom_stem");
	public List<String> whitelisted_log_block_tags = List.of("minecraft:logs");
	public List<String> whitelisted_leaves_blocks = List.of("minecraft:red_mushroom_block", "minecraft:brown_mushroom_block");
	public List<String> whitelisted_leaves_block_tags = List.of("minecraft:leaves", "minecraft:wart_blocks");

	public List<String> blacklisted_log_blocks = new ArrayList<>();
	public List<String> blacklisted_leaves_blocks = new ArrayList<>();

	public float food_exhaustion_multiplier = 1f;
	public float food_exhaustion_limit = 4f;

	public float item_damage_multiplier = 1f;

	public boolean tree_mining_speed_by_log_amount = true;
	public int tree_mining_speed_max_log_limit = 10;
	public float tree_mining_speed_multiplier = 1f;
	@ConfigEntry.Gui.CollapsibleObject
	public TreeLimit tree_limit = new TreeLimit();

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
