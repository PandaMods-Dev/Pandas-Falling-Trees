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

package me.pandamods.fallingtrees.api;

import me.pandamods.fallingtrees.registry.TreeTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class TreeRegistry {
	public static Optional<Tree<?>> getTree(BlockState blockState) {
		return TreeTypeRegistry.TREE_REGISTRY.stream().filter(treeType -> treeType.enabled() && treeType.mineableBlock(blockState)).findFirst();
	}

	public static Optional<Tree<?>> getTree(ResourceLocation resourceLocation) {
		return TreeTypeRegistry.TREE_REGISTRY.getOptional(resourceLocation);
	}

	public static ResourceLocation getTreeLocation(Tree<?> tree) {
		return TreeTypeRegistry.TREE_REGISTRY.getKey(tree);
	}
}
