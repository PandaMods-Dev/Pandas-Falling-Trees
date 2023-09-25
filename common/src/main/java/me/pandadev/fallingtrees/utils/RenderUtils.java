package me.pandadev.fallingtrees.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandadev.fallingtrees.mixin.renderer.BlockRenderDispatcherAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class RenderUtils {
	public static final BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

	public static void renderBlock(PoseStack poseStack, BlockState state, BlockAndTintGetter blockAndTintGetter, BlockPos pos,
								   MultiBufferSource multiBufferSource, int overlay, int light) {
		BakedModel bakedModel = blockRenderer.getBlockModel(state);
		int k = ((BlockRenderDispatcherAccessor)blockRenderer).getBlockColors().getColor(state, blockAndTintGetter, pos, 0);
		float r = (float)(k >> 16 & 0xFF) / 255.0f;
		float g = (float)(k >> 8 & 0xFF) / 255.0f;
		float b = (float)(k & 0xFF) / 255.0f;
		blockRenderer.getModelRenderer().renderModel(poseStack.last(),
				multiBufferSource.getBuffer(ItemBlockRenderTypes.getRenderType(state, true)), state, bakedModel, r, g, b, overlay, light);
	}

	public static float getDeltaSeconds() {
		return 1 / ((float) Minecraft.getInstance().getFps());
	}
}
