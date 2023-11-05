package me.pandamods.fallingtrees.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.pandamods.fallingtrees.entity.TreeEntity;
import me.pandamods.fallingtrees.utils.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import org.joml.Math;

public class TreeRenderer extends EntityRenderer<TreeEntity> {
	public TreeRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(TreeEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();

		float time = this.getBob(entity, partialTick) / 30;
		float animationTime = (float) Math.min(Math.PI*3, time * (time / 3));
		float animation = (-Math.abs(Math.sin(animationTime) / animationTime) + 1) * -90;
		poseStack.mulPose(Axis.XP.rotationDegrees(animation));

		poseStack.translate(-.5, 0, -.5);
		VertexConsumer consumer = buffer.getBuffer(RenderType.translucent());
		entity.getBlocks().forEach((blockPos, blockState) -> {
			poseStack.pushPose();
			poseStack.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
			RenderUtils.renderBlock(poseStack, blockState, blockPos.offset(entity.getOriginPos()), entity.level(), consumer);
			poseStack.popPose();
		});
		poseStack.popPose();
	}

	protected float getBob(TreeEntity entity, float partialTick) {
		return entity.tickCount + partialTick;
	}

	@Override
	public ResourceLocation getTextureLocation(TreeEntity entity) {
		return null;
	}
}
