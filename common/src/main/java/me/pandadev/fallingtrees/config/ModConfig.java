package me.pandadev.fallingtrees.config;

import me.pandadev.fallingtrees.FallingTrees;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = FallingTrees.MOD_ID)
public class ModConfig extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    public ClientConfig client = new ClientConfig();

    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.TransitiveObject
    public ServerConfig server = new ServerConfig();
}
