package me.pandadev.fallingtrees.compat;

import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.compat.jade.TreeDropComponentProvider;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadeCompat implements IWailaPlugin {
	@Override
	public void register(IWailaCommonRegistration registration) {

	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.hideTarget(FallingTrees.TREE_ENTITY.get());
		registration.registerBlockComponent(TreeDropComponentProvider.INSTANCE, Block.class);
	}
}
