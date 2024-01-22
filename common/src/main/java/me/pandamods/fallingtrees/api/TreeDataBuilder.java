package me.pandamods.fallingtrees.api;

import me.pandamods.fallingtrees.config.FallingTreesConfig;
import net.minecraft.core.BlockPos;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TreeDataBuilder {
	private Set<BlockPos> blocks = new HashSet<>();
	private boolean useDefaultMiningSpeed = true;
	private float miningSpeedMultiplication = 1;

	public TreeDataBuilder setBlocks(Set<BlockPos> blocks) {
		this.blocks = blocks;
		return this;
	}

	public void setMiningSpeed(float multiply) {
		this.useDefaultMiningSpeed = false;
		this.miningSpeedMultiplication = multiply;
	}

	public TreeDataBuilder addBlocks(Collection<BlockPos> blocks) {
		this.blocks.addAll(blocks);
		return this;
	}

	public TreeDataBuilder addBlock(BlockPos blockPos) {
		this.blocks.add(blockPos);
		return this;
	}

	public TreeData build(boolean shouldFall) {
		return new TreeData(blocks, useDefaultMiningSpeed ? getDefaultMiningSpeed() : miningSpeedMultiplication, shouldFall);
	}

	protected float getDefaultMiningSpeed() {
		float speedMultiplication = FallingTreesConfig.getCommonConfig().dynamicMiningSpeed.speedMultiplication;
		return 1f / (((float) blocks.size() - 1f) * speedMultiplication + 1f);
	}
}