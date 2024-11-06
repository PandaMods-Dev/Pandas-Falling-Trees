/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.fallingtrees.neoforge;

import me.pandamods.fallingtrees.FallingTrees;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(FallingTrees.MOD_ID)
public class FallingTreesNeoForge {
	public final static DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA =
			DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, FallingTrees.MOD_ID);

    public FallingTreesNeoForge(IEventBus eventBus) {
		eventBus.addListener(FMLCommonSetupEvent.class, event -> commonSetup(event, eventBus));
    }

	public static void commonSetup(FMLCommonSetupEvent event, IEventBus eventBus) {
		FallingTrees.init();

		ENTITY_DATA.register(eventBus);
	}
}
