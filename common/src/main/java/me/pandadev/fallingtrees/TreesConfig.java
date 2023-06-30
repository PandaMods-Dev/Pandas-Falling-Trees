package me.pandadev.fallingtrees;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

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

	@ConfigEntry.Gui.CollapsibleObject
	public Common common = new Common();

	@ConfigEntry.Gui.CollapsibleObject
	public Client client = new Client();

	public static class Common {
		public boolean allow_one_block_mining = true;

		public String[] whitelisted_log_blocks = new String[] {};
		public String[] whitelisted_log_block_tags = new String[] {
				"minecraft:log"
		};

		public String[] whitelisted_leaves_blocks = new String[] {};
		public String[] whitelisted_leaves_block_tags = new String[] {
				"minecraft:leaves"
		};

		public String[] blacklisted_log_blocks = new String[] {};
		public String[] blacklisted_leaves_blocks = new String[] {};
	}

	public static class Client {
		public OneBlockMiningEnum one_block_mining_method = OneBlockMiningEnum.CROUCH;
	}

	public enum OneBlockMiningEnum {
		CROUCH,
		KEYBIND_TOGGLE,
		OFF
	}
}
