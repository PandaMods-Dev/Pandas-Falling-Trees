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
	private int toolDamage = 0;
	private float foodExhaustionMultiplication = 1;
	private int awardedBlocks = 0;

	public Set<BlockPos> getBlocks() {
		return blocks;
	}

	public TreeDataBuilder setBlocks(Set<BlockPos> blocks) {
		this.blocks = blocks;
		return this;
	}

	public TreeDataBuilder setMiningSpeed(float multiply) {
		this.useDefaultMiningSpeed = false;
		this.miningSpeedMultiplication = multiply;
		return this;
	}

	public TreeDataBuilder addBlocks(Collection<BlockPos> blocks) {
		this.blocks.addAll(blocks);
		return this;
	}

	public TreeDataBuilder addBlock(BlockPos blockPos) {
		this.blocks.add(blockPos);
		return this;
	}

	public TreeDataBuilder setToolDamage(int toolDamage) {
		this.toolDamage = toolDamage;
		return this;
	}

	public TreeDataBuilder setFoodExhaustion(float multiply) {
		this.foodExhaustionMultiplication = multiply;
		return this;
	}

	public TreeDataBuilder setAwardedBlocks(int awardedBlocks) {
		this.awardedBlocks = awardedBlocks;
		return this;
	}

	public TreeData build(boolean shouldFall) {
		return new TreeData(
				blocks,
				useDefaultMiningSpeed ? getDefaultMiningSpeed() : miningSpeedMultiplication,
				shouldFall,
				toolDamage,
				foodExhaustionMultiplication,
				awardedBlocks
		);
	}

	protected float getDefaultMiningSpeed() {
		float speedMultiplication = FallingTreesConfig.getCommonConfig().dynamicMiningSpeed.speedMultiplication;
		return 1f / (((float) blocks.size() - 1f) * speedMultiplication + 1f);
	}
}