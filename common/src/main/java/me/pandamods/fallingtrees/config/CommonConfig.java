package me.pandamods.fallingtrees.config;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.config.common.FeaturesConfig;
import me.pandamods.fallingtrees.config.common.FilterConfig;
import me.pandamods.fallingtrees.config.common.LimitationsConfig;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.config.api.Config;
import me.pandamods.pandalib.config.api.ConfigData;

@Config(name = FallingTrees.MOD_ID + "_common", modId = PandaLib.MOD_ID, synchronize = true)
public class CommonConfig implements ConfigData {
	public boolean isCrouchMiningAllowed = true;
	public boolean multiplyToolDamage = true;
	public boolean multiplyFoodExhaustion = true;

	public float treeLifetimeLength = 4;

	public FilterConfig filter = new FilterConfig();

	public LimitationsConfig limitations = new LimitationsConfig();

	public FeaturesConfig features = new FeaturesConfig();
}
