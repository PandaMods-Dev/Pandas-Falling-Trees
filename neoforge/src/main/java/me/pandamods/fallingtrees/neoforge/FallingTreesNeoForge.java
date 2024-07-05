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
import me.pandamods.fallingtrees.client.render.TreeRenderer;
import me.pandamods.fallingtrees.compat.neoforge.CompatNeoForge;
import me.pandamods.fallingtrees.registry.EntityRegistry;
import me.pandamods.fallingtrees.utils.BlockMapEntityData;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(FallingTrees.MOD_ID)
public class FallingTreesNeoForge {
	public final static DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA =
			DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, FallingTrees.MOD_ID);

	public final static DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<?>> BLOCK_MAP_ENTITY_DATA = ENTITY_DATA
			.register("block_map", () -> BlockMapEntityData.BLOCK_MAP);

    public FallingTreesNeoForge(IEventBus modBus) {
		FallingTrees.init();
		CompatNeoForge.init();

		ENTITY_DATA.register(modBus);
		#if MC_VER < MC_1_21
			modBus.addListener(FallingTreesNeoForge::registerRenderers);
		#endif
    }

	#if MC_VER < MC_1_21
		@SubscribeEvent
		public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
			event.registerEntityRenderer(EntityRegistry.TREE.get(), TreeRenderer::new);
		}
	#endif
}
