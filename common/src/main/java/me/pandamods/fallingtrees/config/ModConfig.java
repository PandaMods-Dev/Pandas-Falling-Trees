package me.pandamods.fallingtrees.config;

import me.pandamods.fallingtrees.FallingTrees;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

import static me.shedaniel.autoconfig.util.Utils.getUnsafely;

@Config(name = FallingTrees.MOD_ID)
public class ModConfig extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    public ClientConfig client = new ClientConfig();

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.TransitiveObject
    public CommonConfig common = new CommonConfig();
}
