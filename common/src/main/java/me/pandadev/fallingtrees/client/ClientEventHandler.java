package me.pandadev.fallingtrees.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.event.events.client.ClientGuiEvent;
import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.config.ClientConfig;
import me.pandadev.fallingtrees.utils.PlayerExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public class ClientEventHandler {
	public static void init() {
		ClientGuiEvent.RENDER_HUD.register(ClientEventHandler::HudRender);
	}

	private static void HudRender(GuiGraphics guiGraphics, float v) {
		renderIndicator(guiGraphics, v);
	}

	private static void renderIndicator(GuiGraphics guiGraphics, float v) {
		PoseStack stack = guiGraphics.pose();
		Minecraft minecraft = Minecraft.getInstance();
		ClientConfig.TreeFallIndicator treeFallIndicatorSettings = FallingTrees.getClientConfig().tree_fall_indicator;
		if (treeFallIndicatorSettings.indicator_visible && ((PlayerExtension) minecraft.player).isMiningOneBlock()) {
			Vector2i position = new Vector2i(treeFallIndicatorSettings.hud_position_x, treeFallIndicatorSettings.hud_position_y);
			Vector2f anchor = new Vector2f(treeFallIndicatorSettings.hud_anchor_x, treeFallIndicatorSettings.hud_anchor_y);
			position.add((int) (minecraft.getWindow().getGuiScaledWidth() * anchor.x), (int) (minecraft.getWindow().getGuiScaledHeight() * anchor.y));

			stack.pushPose();
			int size = treeFallIndicatorSettings.size;
			guiGraphics.blit(new ResourceLocation(FallingTrees.MOD_ID, "textures/gui/tree_fall_mode_indicator_v2.png"),
					position.x, position.y, 0, 0, 0, size, size, size, size);
			stack.popPose();
		}
	}
}
