package me.pandadev.fallingtrees.compat.jade;

import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.tree.TreeCache;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum TreeDropComponentProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
		TreeCache cache = TreeCache.getOrCreateCache("jade", blockAccessor.getPosition(), blockAccessor.getLevel(), Minecraft.getInstance().player);
		if (FallingTrees.getClientConfig().jade_support && cache != null && cache.treeType().blockChecker(blockAccessor.getPosition(), blockAccessor.getLevel())) {
			iTooltip.add(iTooltip.getElementHelper().item(new ItemStack(blockAccessor.getBlock().asItem(), cache.getLogAmount())));
		}
	}

	@Override
	public ResourceLocation getUid() {
		return new ResourceLocation(FallingTrees.MOD_ID, "tree_drops");
	}
}
