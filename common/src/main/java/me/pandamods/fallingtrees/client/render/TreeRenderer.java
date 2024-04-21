package me.pandamods.fallingtrees.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.fallingtrees.api.Tree;
import me.pandamods.fallingtrees.config.ClientConfig;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.entity.TreeEntity;
import me.pandamods.fallingtrees.utils.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
	public TreeRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	public ClientConfig getConfig() {
		return FallingTreesConfig.getClientConfig(Minecraft.getInstance().player);
	}

	@Override
	public void render(TreeEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
		Tree tree = entity.getTree();
		if (tree == null) return;

		poseStack.pushPose();

		Map<BlockPos, BlockState> blocks = entity.getBlocks();
		float fallAnimLength = getConfig().animation.fallAnimLength;

		float bounceHeight = getConfig().animation.bounceAngleHeight;
		float bounceAnimLength = getConfig().animation.bounceAnimLength;

		float time = (float) (entity.getLifetime(partialTick) * (Math.PI / 2) / fallAnimLength);

		float fallAnim = bumpCos(time) * 90;
		float bounceAnim = bumpSin((float) ((time - Math.PI / 2) / (bounceAnimLength / (fallAnimLength * 2)))) * bounceHeight;

		float animation = (fallAnim + bounceAnim) - 90;

		Direction direction = entity.getDirection().getOpposite();
		int distance = getDistance(tree, blocks, 0, direction.getOpposite());

		Vector3f pivot =  new Vector3f(0, 0, (.5f + distance) * tree.fallAnimationEdgeDistance());
		pivot.rotateY(Math.toRadians(-direction.toYRot()));
		poseStack.translate(-pivot.x, 0, -pivot.z);

		Vector3f vector = new Vector3f(Math.toRadians(animation), 0, 0);
		vector.rotateY(Math.toRadians(-direction.toYRot()));
		Quaternionf quaternion = new Quaternionf().identity().rotateX(vector.x).rotateZ(vector.z);
		poseStack.mulPose(quaternion);

		poseStack.translate(pivot.x, 0, pivot.z);

		poseStack.translate(-.5, 0, -.5);
		blocks.forEach((blockPos, blockState) -> {
			poseStack.pushPose();
			poseStack.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());

			blockPos = blockPos.offset(entity.getOriginPos());
			RenderUtils.renderSingleBlock(poseStack, blockState, blockPos, entity.level(), buffer, packedLight);

			poseStack.popPose();
		});
		poseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(TreeEntity entity) {
		return null;
	}

	private int getDistance(Tree tree, Map<BlockPos, BlockState> blocks, int distance, Direction direction) {
		BlockPos nextBlockPos = new BlockPos(direction.getNormal().multiply(distance + 1));
		if (blocks.containsKey(nextBlockPos) && tree.mineableBlock(blocks.get(nextBlockPos)))
			return getDistance(tree, blocks, distance + 1, direction);
		return distance;
	}

	private float bumpCos(float time) {
		return (float) Math.max(0, Math.cos(Math.clamp(-Math.PI, Math.PI, time)));
	}

	private float bumpSin(float time) {
		return (float) Math.max(0, Math.sin(Math.clamp(-Math.PI, Math.PI, time)));
	}
}
