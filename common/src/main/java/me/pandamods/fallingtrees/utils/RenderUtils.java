/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.fallingtrees.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
#if MC_VER >= MC_1_20
import net.minecraft.world.item.ItemDisplayContext;
#else
import net.minecraft.client.renderer.block.model.ItemTransforms;
#endif
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

import java.util.BitSet;
import java.util.List;

public class RenderUtils {
	public static void renderSingleBlock(PoseStack poseStack, BlockState blockState, BlockPos blockPos,
										 BlockAndTintGetter level, MultiBufferSource bufferSource, int packedLight) {
		BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
		BlockEntityWithoutLevelRenderer blockEntityRenderDispatcher = blockRenderDispatcher.blockEntityRenderer;
		RenderShape renderShape = blockState.getRenderShape();
		if (renderShape != RenderShape.INVISIBLE) {
			switch (renderShape) {
				case MODEL:
					BakedModel bakedModel = blockRenderDispatcher.getBlockModel(blockState);
					int i = blockRenderDispatcher.blockColors.getColor(blockState, level, blockPos, 0);
					float f = (float)(i >> 16 & 0xFF) / 255.0f;
					float g = (float)(i >> 8 & 0xFF) / 255.0f;
					float h = (float)(i & 0xFF) / 255.0f;
					blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(),
							bufferSource.getBuffer(ItemBlockRenderTypes.getRenderType(blockState, false)), blockState,
							bakedModel, f, g, h, packedLight, OverlayTexture.NO_OVERLAY);
					break;
				case ENTITYBLOCK_ANIMATED:
					blockEntityRenderDispatcher.renderByItem(new ItemStack(blockState.getBlock()),
							#if MC_VER >= MC_1_20 ItemDisplayContext.NONE #else ItemTransforms.TransformType.NONE #endif,
							poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
			}

		}
	}
}
