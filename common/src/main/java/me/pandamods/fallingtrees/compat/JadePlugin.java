package me.pandamods.fallingtrees.compat;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.api.Tree;
import me.pandamods.fallingtrees.api.TreeData;
import me.pandamods.fallingtrees.api.TreeDataBuilder;
import me.pandamods.fallingtrees.api.TreeRegistry;
import me.pandamods.fallingtrees.registry.EntityRegistry;
import me.pandamods.fallingtrees.utils.TreeCache;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.config.IWailaConfig;

@WailaPlugin
public class JadePlugin implements IWailaPlugin, IBlockComponentProvider {
	public static final ResourceLocation hideEntity = new ResourceLocation(FallingTrees.MOD_ID, "hide_entity");
//	public static final ResourceLocation showDrops = new ResourceLocation(FallingTrees.MOD_ID, "show_drops");
	private static final ResourceLocation UID = new ResourceLocation(FallingTrees.MOD_ID, "plugin");

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.addConfig(hideEntity, true);
//		registration.addConfig(showDrops, true);
		registration.markAsClientFeature(hideEntity);
//		registration.markAsClientFeature(showDrops);

		if (IWailaConfig.get().getPlugin().get(hideEntity))
			registration.hideTarget(EntityRegistry.TREE.get());

//		if (IWailaConfig.get().getPlugin().get(showDrops))
//			registration.registerBlockComponent(this, Block.class);
	}

	@Override
	public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
		BlockState blockState = blockAccessor.getBlockState();
		BlockPos blockPos = blockAccessor.getPosition();

		TreeRegistry.getTree(blockState).ifPresent(tree -> {
			TreeData treeData = TreeCache.get(blockAccessor.getPlayer(), blockPos, () ->
					tree.getTreeData(new TreeDataBuilder(), blockPos, blockAccessor.getLevel()));
			if (!treeData.shouldFall()) return;
		});
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}
}
