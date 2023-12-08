package me.pandamods.fallingtrees.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.fallingtrees.api.TreeType;
import me.pandamods.fallingtrees.config.ClientConfig;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.entity.TreeEntity;
import me.pandamods.fallingtrees.utils.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class TreeRenderer extends EntityRenderer<TreeEntity> {
	final ClientConfig config;

	public TreeRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.config = FallingTreesConfig.getClientConfig();
	}

	@Override
	public void render(TreeEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		TreeType treeType = entity.getTreeType();
		if (treeType == null) return;

		poseStack.pushPose();

		Map<BlockPos, BlockState> blocks = entity.getBlocks();
		float fallAnimLength = config.animation.fallAnimLength;

		float bounceHeight = config.animation.bounceAngleHeight;
		float bounceAnimLength = config.animation.bounceAnimLength;

		float time = (float) (entity.getLifetime(partialTick) * (Math.PI / 2) / fallAnimLength);

		float fallAnim = bumpCos(time) * 90;
		float bounceAnim = bumpSin((float) ((time - Math.PI / 2) / (bounceAnimLength / (fallAnimLength * 2)))) * bounceHeight;

		float animation = (fallAnim + bounceAnim) - 90;

		Direction direction = entity.getDirection().getOpposite();
		int distance = getDistance(treeType, blocks, 0, direction.getOpposite());

		Vector3f pivot =  new Vector3f(0, 0, (.5f + distance) * treeType.fallAnimationEdgeDistance());
		pivot.rotateY(Math.toRadians(-direction.toYRot()));
		poseStack.translate(-pivot.x, 0, -pivot.z);

		Vector3f vector = new Vector3f(Math.toRadians(animation), 0, 0);
		vector.rotateY(Math.toRadians(-direction.toYRot()));
		Quaternionf quaternion = new Quaternionf().identity().rotateX(vector.x).rotateZ(vector.z);
		poseStack.mulPose(quaternion);

		poseStack.translate(pivot.x, 0, pivot.z);

		poseStack.translate(-.5, 0, -.5);
		VertexConsumer consumer = buffer.getBuffer(RenderType.cutout());
		blocks.forEach((blockPos, blockState) -> {
			poseStack.pushPose();
			poseStack.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
			RenderUtils.renderBlock(poseStack, blockState, blockPos.offset(entity.getOriginPos()), entity.level(), consumer,
					(state, level, offset, face, pos) -> {
						if (state.canOcclude()) {
							BlockPos facePos = blockPos.offset(face.getNormal());
							if (blocks.containsKey(facePos)) {
								return !state.is(blocks.get(facePos).getBlock());
							}
							return true;
						}
						return true;
					});
			poseStack.popPose();
		});
		poseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(TreeEntity entity) {
		return null;
	}

	private int getDistance(TreeType treeType, Map<BlockPos, BlockState> blocks, int distance, Direction direction) {
		BlockPos nextBlockPos = new BlockPos(direction.getNormal().multiply(distance + 1));
		if (blocks.containsKey(nextBlockPos) && treeType.baseBlockCheck(blocks.get(nextBlockPos)))
			return getDistance(treeType, blocks, distance + 1, direction);
		return distance;
	}

	private float bumpCos(float time) {
		return (float) Math.max(0, Math.cos(Math.clamp(-Math.PI, Math.PI, time)));
	}

	private float bumpSin(float time) {
		return (float) Math.max(0, Math.sin(Math.clamp(-Math.PI, Math.PI, time)));
	}
}
