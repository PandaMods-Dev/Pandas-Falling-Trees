package me.pandamods.fallingtrees.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.BitSet;
import java.util.List;
import java.util.Random;

public class RenderUtils {
	public static void renderBlock(PoseStack poseStack, BlockState blockState, BlockPos blockPos,
								   BlockAndTintGetter level, MultiBufferSource bufferSource, RandomSource random) {
		BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
		ModelBlockRenderer blockRenderer = blockRenderDispatcher.getModelRenderer();
		BakedModel model = blockRenderDispatcher.getBlockModel(blockState);
		VertexConsumer vertexConsumer = bufferSource.getBuffer(ItemBlockRenderTypes.getRenderType(blockState, false));
		long seed = blockState.getSeed(blockPos);

		BitSet bitSet = new BitSet(3);
		BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
		for (Direction direction : Direction.values()) {
			random.setSeed(seed);
			List<BakedQuad> quads = model.getQuads(blockState, direction, random);
			if (quads.isEmpty()) continue;
			mutableBlockPos.setWithOffset(blockPos, direction);
			boolean isSolid = !level.getBlockState(mutableBlockPos).isAir() && level.getBlockState(mutableBlockPos).isSolidRender(level, mutableBlockPos);
			int lightColor = LevelRenderer.getLightColor(level, blockState, isSolid ? blockPos : mutableBlockPos);
			blockRenderer.renderModelFaceFlat(level, blockState, blockPos, lightColor, OverlayTexture.NO_OVERLAY, false,
					poseStack, vertexConsumer, quads, bitSet);
		}
		random.setSeed(seed);
		List<BakedQuad> quads = model.getQuads(blockState, null, random);
		if (!quads.isEmpty()) {
			blockRenderer.renderModelFaceFlat(level, blockState, blockPos, -1, OverlayTexture.NO_OVERLAY, true,
					poseStack, vertexConsumer, quads, bitSet);
		}
	}
}
