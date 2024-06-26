package me.pandamods.fallingtrees.compat;

import me.pandamods.fallingtrees.registry.EntityRegistry;
import snownee.jade.api.*;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.hideTarget(EntityRegistry.TREE.get());
	}
}
