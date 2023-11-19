package me.pandamods.fallingtrees.config;

import me.pandamods.fallingtrees.FallingTrees;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = FallingTrees.MOD_ID + "_client")
public class ClientConfig implements ConfigData {
	public boolean invertCrouchMining = false;

	@ConfigEntry.Category("sound_settings")
	@ConfigEntry.Gui.TransitiveObject
	public SoundSettings soundSettings = new SoundSettings();

	public static class SoundSettings {
		public boolean enabled = true;
		public float startVolume = 0.7f;
		public float endVolume = 0.7f;
	}
}
