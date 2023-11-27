package me.pandamods.fallingtrees.config.screen;

import me.pandamods.fallingtrees.config.ClientConfig;
import me.pandamods.fallingtrees.config.CommonConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.telemetry.TelemetryInfoScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

@Environment(value = EnvType.CLIENT)
public class ConfigScreen extends Screen {
	private final Screen parent;

	public ConfigScreen(Screen parent) {
		super(Component.translatable("config.fallingtrees.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter();
		GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(2);

		Button clientConfigButton = Button.builder(
				Component.translatable("config.fallingtrees.client"),
				button ->
						this.minecraft.setScreen(AutoConfig.getConfigScreen(ClientConfig.class, this).get())
		).build();

		Button commonConfigButton = Button.builder(
				Component.translatable("config.fallingtrees.common"),
				button ->
						this.minecraft.setScreen(AutoConfig.getConfigScreen(CommonConfig.class, this).get())
		).build();

		if (this.minecraft.level != null) {
			commonConfigButton.active = false;
			commonConfigButton.setTooltip(Tooltip.create(Component.translatable("config.fallingtrees.common.tooltip")));
		}

		rowHelper.addChild(clientConfigButton);
		rowHelper.addChild(commonConfigButton);

		rowHelper.addChild(SpacerElement.height(12), 2);

		rowHelper.addChild(Button.builder(CommonComponents.GUI_DONE, button -> this.minecraft.setScreen(parent)).build(), 2);

		gridLayout.arrangeElements();
		FrameLayout.alignInRectangle(gridLayout, 0, this.height / 3 - 12, this.width, this.height, 0.5f, 0.0f);
		gridLayout.visitWidgets(this::addRenderableWidget);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.renderDirtBackground(guiGraphics);
		guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(parent);
	}

	private <T extends ConfigData> Button openConfigScreen(Component text, Class<T> configClass) {
		return Button.builder(text, button -> this.minecraft.setScreen(AutoConfig.getConfigScreen(configClass, this).get())).build();
	}
}
