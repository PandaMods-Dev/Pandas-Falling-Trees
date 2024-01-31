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
import snownee.jade.api.view.IServerExtensionProvider;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.hideTarget(EntityRegistry.TREE.get());
	}
}
