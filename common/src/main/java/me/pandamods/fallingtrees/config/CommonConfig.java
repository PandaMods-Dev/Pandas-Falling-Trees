package me.pandamods.fallingtrees.config;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.config.common.TreeConfigs;
import me.pandamods.pandalib.config.api.Config;
import me.pandamods.pandalib.config.api.ConfigData;

@Config(name = FallingTrees.MOD_ID + "_common", modId = FallingTrees.MOD_ID, synchronize = true)
public class CommonConfig implements ConfigData {
	public boolean disableCrouchMining = true;
	public boolean multiplyToolDamage = true;
	public boolean multiplyFoodExhaustion = true;
	public boolean onlyFallWithRequiredTool = false;

	public float treeLifetimeLength = 4;

	public DynamicMiningSpeed dynamicMiningSpeed = new DynamicMiningSpeed();
	public TreeConfigs trees = new TreeConfigs();

	public static class DynamicMiningSpeed {
		public boolean disable = false;
		public float speedMultiplication = 0.5f;
		public float minimumSpeed = 0.0001f;
	}
}
