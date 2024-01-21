package me.pandamods.fallingtrees.api;

import net.minecraft.core.BlockPos;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TreeDataBuilder {
	private Set<BlockPos> blocks = new HashSet<>();
	private float miningSpeedMultiplication = 0;

	public TreeDataBuilder setBlocks(Set<BlockPos> blocks) {
		this.blocks = blocks;
		return this;
	}

	public void setMiningSpeed(float multiply) {
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
		return new TreeData(blocks, miningSpeedMultiplication, shouldFall);
	}
}