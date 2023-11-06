package me.pandamods.fallingtrees.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "client")
public class ClientConfig implements ConfigData {
	public boolean invertCrouchMining = false;
}
