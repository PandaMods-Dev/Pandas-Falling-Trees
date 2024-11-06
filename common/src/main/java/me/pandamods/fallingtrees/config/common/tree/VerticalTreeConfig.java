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

package me.pandamods.fallingtrees.config.common.tree;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class VerticalTreeConfig extends TreeConfig {
	public Filter filter = new Filter(
			new ArrayList<>(),
				List.of(BuiltInRegistries.BLOCK.getKey(Blocks.CACTUS).toString(), BuiltInRegistries.BLOCK.getKey(Blocks.BAMBOO).toString()),
			new ArrayList<>()
	);
}
