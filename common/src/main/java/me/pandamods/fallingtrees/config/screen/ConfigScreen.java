package me.pandamods.fallingtrees.config.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.fallingtrees.config.ClientConfig;
import me.pandamods.fallingtrees.config.CommonConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(value = EnvType.CLIENT)
public class ConfigScreen extends Screen {
	private final Screen parent;

	public ConfigScreen(Screen parent) {
		super(Component.translatable("config.fallingtrees.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		Button clientConfigButton = new Button(this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20,
				Component.translatable("config.fallingtrees.client"),
				button ->
						this.minecraft.setScreen(AutoConfig.getConfigScreen(ClientConfig.class, this).get())
		);

		Button commonConfigButton = new Button(this.width / 2 + 5, this.height / 6 + 48 - 6, 150, 20,
				Component.translatable("config.fallingtrees.common"),
				button ->
						this.minecraft.setScreen(AutoConfig.getConfigScreen(CommonConfig.class, this).get()),
				(button, poseStack, i, j) -> {
					if (!button.active) {
						this.renderTooltip(poseStack, Component.translatable("config.fallingtrees.common.tooltip"), i, j);
					}
				}
		);

		Button backButton = new Button(this.width / 2 - 100, this.height / 6 + 48 - 6 + 30, 200, 20,
				CommonComponents.GUI_BACK,
				button -> this.minecraft.setScreen(parent)
		);

		if (this.minecraft.level != null) {
			commonConfigButton.active = false;
		}

		this.addRenderableWidget(clientConfigButton);
		this.addRenderableWidget(commonConfigButton);

		this.addRenderableWidget(backButton);
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		this.renderDirtBackground(0);
		GuiComponent.drawCenteredString(poseStack, this.font, this.title, this.width / 2, 15, 0xFFFFFF);
		super.render(poseStack, mouseX, mouseY, partialTick);
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(parent);
	}
}
