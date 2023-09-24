package me.pandadev.fallingtrees;

import com.google.gson.Gson;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.pandadev.fallingtrees.config.ClientConfig;
import me.pandadev.fallingtrees.network.PacketHandler;
import me.pandadev.fallingtrees.tree.TreeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class EventHandler {
	public static void init() {
		PlayerEvent.PLAYER_JOIN.register(EventHandler::onPlayerJoin);
	}

	public static void clientInit() {
		ClientGuiEvent.RENDER_HUD.register(EventHandler::HudRender);
	}

	private static void onPlayerJoin(ServerPlayer serverPlayer) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeByteArray(new Gson().toJson(FallingTrees.getServerConfig()).getBytes());
		NetworkManager.sendToPlayer(serverPlayer, PacketHandler.CONFIG_PACKET_ID, buf);
	}

	public static void HudRender(GuiGraphics guiGraphics, float v) {
		renderIndicator(guiGraphics, v);
	}

	private static void renderIndicator(GuiGraphics guiGraphics, float v) {
		PoseStack stack = guiGraphics.pose();
		Minecraft minecraft = Minecraft.getInstance();
		ClientConfig.TreeFallIndicator treeFallIndicatorSettings = FallingTrees.getClientConfig().tree_fall_indicator;
		if (treeFallIndicatorSettings.indicator_visible && TreeUtils.isMiningOneBlock(minecraft.player)) {
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
