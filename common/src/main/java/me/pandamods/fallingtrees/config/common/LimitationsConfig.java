package me.pandamods.fallingtrees.config.common;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class LimitationsConfig {
	public int maxLeavesDistance = 7;

	@ConfigEntry.Gui.CollapsibleObject
	public TreeFallRequirements treeFallRequirements = new TreeFallRequirements();

	public static class TreeFallRequirements {
		@ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
		public RequiredBlockType maxAmountType = RequiredBlockType.BASE_BLOCK_AMOUNT;
		public int maxAmount = 200;
		public boolean onlyRequiredTool = false;

		public enum RequiredBlockType {
			BASE_BLOCK_AMOUNT,
			BLOCK_AMOUNT
		}
	}
}
