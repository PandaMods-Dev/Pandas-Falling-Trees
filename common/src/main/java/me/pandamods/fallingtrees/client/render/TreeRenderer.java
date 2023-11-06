package me.pandamods.fallingtrees.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.pandamods.fallingtrees.api.TreeType;
import me.pandamods.fallingtrees.entity.TreeEntity;
import me.pandamods.fallingtrees.utils.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TreeRenderer extends EntityRenderer<TreeEntity> {
	public TreeRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(TreeEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();

		Map<BlockPos, BlockState> blocks = entity.getBlocks();
		float time = this.getBob(entity, partialTick) / 20;
		float animationTime = (float) Math.min(Math.PI*3, time * (time / 3));
		float animation = (-Math.abs(Math.sin(animationTime) / animationTime) + 1) * -90;

		Direction direction = entity.getDirection();
		int distance = getDistance(entity.getTreeType(), blocks, 0, direction);

		Vector3f pivot =  new Vector3f(0, 0, (float) (.5 + distance));
		pivot.rotateY(Math.toRadians(direction.toYRot()));
		poseStack.translate(-pivot.x, 0, -pivot.z);

		Vector3f vector = new Vector3f(Math.toRadians(animation), 0, 0);
		vector.rotateY(Math.toRadians(direction.toYRot()));
		Quaternionf quaternion = new Quaternionf().identity().rotateX(vector.x).rotateZ(vector.z);
		poseStack.mulPose(quaternion);

		poseStack.translate(pivot.x, 0, pivot.z);

		poseStack.translate(-.5, 0, -.5);
		VertexConsumer consumer = buffer.getBuffer(RenderType.translucent());
		blocks.forEach((blockPos, blockState) -> {
			poseStack.pushPose();
			poseStack.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
			RenderUtils.renderBlock(poseStack, blockState, blockPos.offset(entity.getOriginPos()), entity.level(), consumer, false);
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

	private int getDistance(TreeType treeType, Map<BlockPos, BlockState> blocks, int distance, Direction direction) {
		BlockPos nextBlockPos = new BlockPos(direction.getOpposite().getNormal().multiply(distance + 1));
		if (blocks.containsKey(nextBlockPos) && treeType.baseBlockCheck(blocks.get(nextBlockPos)))
			return getDistance(treeType, blocks, distance + 1, direction);
		return distance;
	}
}
