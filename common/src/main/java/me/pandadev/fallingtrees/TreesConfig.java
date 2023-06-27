package me.pandadev.fallingtrees;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = FallingTrees.MOD_ID)
public class TreesConfig implements ConfigData {
//	@ConfigEntry.Gui.Tooltip()
//	public String[] valid_decorative_blocks = new String[] {
//			"minecraft:cocoa",
//    		"minecraft:vine",
//			"minecraft:bee_nest"
//	};
//
//	@ConfigEntry.Gui.Tooltip()
//	public String[] valid_decorative_block_tags = new String[] {
//	};

	public boolean allow_one_block_mining = true;
}
