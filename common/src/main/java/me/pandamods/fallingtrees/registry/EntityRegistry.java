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

package me.pandamods.fallingtrees.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.entity.TreeEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntityRegistry {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(FallingTrees.MOD_ID,
			Registries.ENTITY_TYPE);

	public static final RegistrySupplier<EntityType<TreeEntity>> TREE = ENTITIES.register("tree", () ->
			EntityType.Builder
					.of(TreeEntity::new, MobCategory.MISC)
					.sized(0.5f, 0.5f)
					.noSave()
					.fireImmune()
					.build(ResourceKey.create(Registries.ENTITY_TYPE, FallingTrees.ID("tree")))
	);
}
