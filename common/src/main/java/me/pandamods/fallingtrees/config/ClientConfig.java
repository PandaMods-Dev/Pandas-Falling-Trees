package me.pandamods.fallingtrees.config;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.config.client.AnimationConfig;
import me.pandamods.fallingtrees.config.client.SoundSettingsConfig;
import me.pandamods.pandalib.config.api.Config;
import me.pandamods.pandalib.config.api.ConfigData;

@Config(name = FallingTrees.MOD_ID + "_client", modId = FallingTrees.MOD_ID, synchronize = true)
public class ClientConfig implements ConfigData {
	public boolean invertCrouchMining = false;
	public SoundSettingsConfig soundSettings = new SoundSettingsConfig();
	public AnimationConfig animation = new AnimationConfig();
}
