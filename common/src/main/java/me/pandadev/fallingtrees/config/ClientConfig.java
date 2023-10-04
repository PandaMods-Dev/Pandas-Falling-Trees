package me.pandadev.fallingtrees.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "client")
public class ClientConfig implements ConfigData {
	public boolean sound_effect = true;
	public float sound_effect_volume = 0.25f;

	public OneBlockMiningEnum one_block_mining_method = OneBlockMiningEnum.SHIFT;

	@ConfigEntry.Gui.Tooltip
	public boolean is_mining_one_block = false;

	@ConfigEntry.Gui.CollapsibleObject
	public TreeFallIndicator tree_fall_indicator = new TreeFallIndicator();

	@ConfigEntry.Gui.Tooltip
	public boolean jade_support = true;

	public enum OneBlockMiningEnum {
		SHIFT,
		KEYBIND_TOGGLE,
		KEYBIND_HOLD
	}

	public static class TreeFallIndicator {
		public boolean indicator_visible = true;

		public int hud_position_x = 92;
		public int hud_position_y = -18;

		public float hud_anchor_x = 0.5f;
		public float hud_anchor_y = 1.0f;

		public int size = 16;
	}
}
