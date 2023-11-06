package me.pandamods.fallingtrees.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "common")
public class CommonConfig implements ConfigData {
	@ConfigEntry.Gui.CollapsibleObject
	public Limit limit = new Limit();

	public boolean isCrouchMiningAllowed = true;
	public boolean damageUsedTool = true;
	public boolean causeFoodExhaustion = true;

	public static class Limit {
		@ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
		public LimitType limitType = LimitType.BASE_BLOCK_AMOUNT;
		public int blockAmountLimit = 200;
		public boolean onlyRequiredTool = false;

		public enum LimitType {
			BASE_BLOCK_AMOUNT,
			BLOCK_AMOUNT
		}
	}
}
