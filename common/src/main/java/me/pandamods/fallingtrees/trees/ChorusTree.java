package me.pandamods.fallingtrees.trees;

import me.pandamods.fallingtrees.api.Tree;
import me.pandamods.fallingtrees.api.TreeData;
import me.pandamods.fallingtrees.api.TreeDataBuilder;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.entity.TreeEntity;
import me.pandamods.fallingtrees.utils.ListUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChorusTree implements Tree {
	public ChorusTree() {
		super();
	}

	@Override
	public boolean mineableBlock(BlockState blockState) {
		return blockState.is(Blocks.CHORUS_PLANT);
	}

	public boolean extraRequiredBlockCheck(BlockState blockState) {
		return blockState.is(Blocks.CHORUS_FLOWER);
	}

	@Override
	public TreeData getTreeData(TreeDataBuilder builder, BlockPos blockPos, BlockGetter level) {
		Set<BlockPos> loopedBlocks = new HashSet<>();

		loopBlocks(level, blockPos, builder, loopedBlocks);
		return builder
				.setAwardedBlocks(builder.getBlocks().size())
				.setFoodExhaustion(builder.getBlocks().size())
				.setToolDamage(builder.getBlocks().size())
				.build(true);
	}

	public void loopBlocks(BlockGetter level, BlockPos originPos, TreeDataBuilder builder, Set<BlockPos> loopedBlocks) {
		if (loopedBlocks.contains(originPos))
			return;

		loopedBlocks.add(originPos);

		BlockState blockState = level.getBlockState(originPos);
		if (this.mineableBlock(blockState) || this.extraRequiredBlockCheck(blockState)) {
			builder.addBlock(originPos);

			if (this.mineableBlock(blockState)) {
				for (Direction direction : Direction.values()) {
					if (blockState.getValue(ChorusPlantBlock.PROPERTY_BY_DIRECTION.get(direction))) {
						BlockPos neighborPos = originPos.offset(direction.getNormal());
						loopBlocks(level, neighborPos, builder, loopedBlocks);
					}
				}
			}
		}
	}

	@Override
	public float fallAnimationEdgeDistance() {
		return 6f / 16f;
	}

	@Override
	public boolean enabled() {
		return FallingTreesConfig.getCommonConfig().trees.chorusTree.enabled;
	}

	@Override
	public List<ItemStack> getDrops(TreeEntity entity, Map<BlockPos, BlockState> blocks) {
		return Tree.super.getDrops(entity, ListUtils.mapRemoveIf(blocks, (blockPos, blockState) -> blockState.is(Blocks.CHORUS_FLOWER)));
	}
}
