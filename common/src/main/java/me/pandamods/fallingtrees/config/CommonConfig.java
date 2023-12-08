package me.pandamods.fallingtrees.config;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.config.common.FeaturesConfig;
import me.pandamods.fallingtrees.config.common.FilterConfig;
import me.pandamods.fallingtrees.config.common.LimitationsConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = FallingTrees.MOD_ID + "_common")
public class CommonConfig implements ConfigData {
	public boolean isCrouchMiningAllowed = true;
	public boolean multiplyToolDamage = true;
	public boolean multiplyFoodExhaustion = true;

	public float treeLifeLength = 4;

	@ConfigEntry.Category("filter")
	@ConfigEntry.Gui.TransitiveObject
	public FilterConfig filter = new FilterConfig();

	@ConfigEntry.Category("limitations")
	@ConfigEntry.Gui.TransitiveObject
	public LimitationsConfig limitations = new LimitationsConfig();

	@ConfigEntry.Category("features")
	@ConfigEntry.Gui.TransitiveObject
	public FeaturesConfig features = new FeaturesConfig();
}
