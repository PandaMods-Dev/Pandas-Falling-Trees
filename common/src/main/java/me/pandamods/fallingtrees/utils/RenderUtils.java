package me.pandamods.fallingtrees.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RenderUtils {
	public static BlockRenderDispatcher getBlockRenderDispatcher() {
		return Minecraft.getInstance().getBlockRenderer();
	}

	public static void renderBlock(PoseStack poseStack, BlockState blockState, BlockPos blockPos,
								   Level level, VertexConsumer vertexConsumer, boolean checkSides) {
		getBlockRenderDispatcher().renderBatched(blockState, blockPos, level, poseStack, vertexConsumer, checkSides, level.getRandom());
	}

	public static float getDeltaTime() {
		return Minecraft.getInstance().getDeltaFrameTime() / 20;
	}
}
