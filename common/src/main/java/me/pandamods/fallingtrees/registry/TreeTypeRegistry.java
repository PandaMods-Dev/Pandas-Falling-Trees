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

import com.mojang.serialization.Lifecycle;
import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.api.Tree;
import me.pandamods.fallingtrees.trees.ChorusTree;
import me.pandamods.fallingtrees.trees.MushroomTree;
import me.pandamods.fallingtrees.trees.StandardTree;
import me.pandamods.fallingtrees.trees.VerticalTree;
import me.pandamods.pandalib.registry.DeferredRegister;
import me.pandamods.pandalib.registry.RegistryRegister;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class TreeTypeRegistry {
	public static final ResourceKey<Registry<Tree<?>>> TREE_REGISTRY_KEY = ResourceKey.createRegistryKey(FallingTrees.ID("tree_registry"));
	public static final Registry<Tree<?>> TREE_REGISTRY = RegistryRegister.register(new MappedRegistry<>(TREE_REGISTRY_KEY, Lifecycle.stable()));
	
	public static final DeferredRegister<Tree<?>> TREES = DeferredRegister.create(FallingTrees.MOD_ID, TREE_REGISTRY_KEY);
	
	public static final Supplier<StandardTree> DEFAULT = TREES.register("default", StandardTree::new);
	public static final Supplier<VerticalTree> VERTICAL = TREES.register("vertical", VerticalTree::new);
	public static final Supplier<ChorusTree> CHORUS = TREES.register("chorus", ChorusTree::new);
	public static final Supplier<MushroomTree> MUSHROOM = TREES.register("mushroom", MushroomTree::new);
}
