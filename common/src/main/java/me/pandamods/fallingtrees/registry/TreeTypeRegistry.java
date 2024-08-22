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

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.api.TreeRegistry;
import me.pandamods.fallingtrees.trees.MushroomTree;
import me.pandamods.fallingtrees.trees.VerticalTree;
import me.pandamods.fallingtrees.trees.ChorusTree;
import me.pandamods.fallingtrees.trees.StandardTree;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class TreeTypeRegistry {

	public static final Supplier<StandardTree> DEFAULT = TreeRegistry.register(FallingTrees.ID("default"), StandardTree::new);
	public static final Supplier<VerticalTree> VERTICAL = TreeRegistry.register(FallingTrees.ID("vertical"), VerticalTree::new);
	public static final Supplier<ChorusTree> CHORUS = TreeRegistry.register(FallingTrees.ID("chorus"), ChorusTree::new);
	public static final Supplier<MushroomTree> MUSHROOM = TreeRegistry.register(FallingTrees.ID("mushroom"), MushroomTree::new);

	public static void register() {
	}
}
