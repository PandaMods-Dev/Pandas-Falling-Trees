package me.pandadev.fallingtrees.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandadev.fallingtrees.entity.TreeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class TreeRenderer extends EntityRenderer<TreeEntity> {
	private final BlockRenderDispatcher blockRenderer;

	public TreeRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.blockRenderer = context.getBlockRenderDispatcher();
	}

	@Override
	public void render(TreeEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		entity.fallTime += Minecraft.getInstance().getDeltaFrameTime()/10;
		poseStack.pushPose();

		Map<BlockPos, BlockState> blocks = entity.getEntityData().get(TreeEntity.BLOCKS);

		float time = (entity.fallTime*entity.fallTime)/15;
		Vector3f rotation = new Vector3f(-Math.toRadians(Math.lerp(90, 0, Math.abs(Math.sin(time)/time))), 0, 0);
		rotation.rotateY(entity.getEntityData().get(TreeEntity.ROTATION));
		poseStack.mulPose(new Quaternionf().identity().rotateXYZ(rotation.x, rotation.y, rotation.z));

		poseStack.pushPose();
		poseStack.translate(-0.5, 0, -0.5);
		for (Map.Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
			poseStack.pushPose();
			poseStack.translate(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ());
			this.blockRenderer.renderSingleBlock(entry.getValue(), poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
			poseStack.popPose();
		}
		poseStack.popPose();
		poseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(TreeEntity entity) {
		return null;
	}
}
