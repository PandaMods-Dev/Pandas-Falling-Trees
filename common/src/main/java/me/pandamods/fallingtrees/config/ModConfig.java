package me.pandamods.fallingtrees.config;

import me.pandamods.fallingtrees.FallingTrees;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Config(name = FallingTrees.MOD_ID)
public class ModConfig extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    public ClientConfig client = new ClientConfig();

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.TransitiveObject
    public CommonConfig common = new CommonConfig();

	public static Screen build(Screen screen) {
		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(screen)
				.setTitle(Component.translatable("title.fallingtrees.config"));

		ConfigCategory client = builder.getOrCreateCategory(Component.translatable("category.fallingtrees.client"));

		builder.setSavingRunnable(() -> {

		});

		return builder.build();
	}
}
