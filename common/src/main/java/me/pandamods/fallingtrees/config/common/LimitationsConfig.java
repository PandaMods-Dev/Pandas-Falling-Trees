package me.pandamods.fallingtrees.config.common;

public class LimitationsConfig {
	public int maxLeavesDistance = 7;

	public TreeFallRequirements treeFallRequirements = new TreeFallRequirements();

	public static class TreeFallRequirements {
		public RequiredBlockType maxAmountType = RequiredBlockType.BASE_BLOCK_AMOUNT;
		public int maxAmount = 500;
		public boolean onlyRequiredTool = false;

		public enum RequiredBlockType {
			BASE_BLOCK_AMOUNT,
			BLOCK_AMOUNT
		}
	}
}
