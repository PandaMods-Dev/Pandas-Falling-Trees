package me.pandamods.fallingtrees.config;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.config.client.AnimationConfig;
import me.pandamods.fallingtrees.config.client.SoundSettingsConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Config(name = FallingTrees.MOD_ID + "_client")
public class ClientConfig implements ConfigData {
	public boolean invertCrouchMining = false;

	@ConfigEntry.Category("sound_settings")
	@ConfigEntry.Gui.TransitiveObject
	public SoundSettingsConfig soundSettings = new SoundSettingsConfig();

	@ConfigEntry.Category("animation")
	@ConfigEntry.Gui.TransitiveObject
	public AnimationConfig animation = new AnimationConfig();
}
