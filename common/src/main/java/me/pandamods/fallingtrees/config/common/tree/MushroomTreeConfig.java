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

import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class MushroomTreeConfig extends TreeConfig {
	public Filter stemFilter = new Filter(
			new ArrayList<>(),
			List.of(Blocks.MUSHROOM_STEM.arch$registryName().toString()),
			new ArrayList<>()
	);
	public Filter capFilter = new Filter(
			new ArrayList<>(),
			List.of(Blocks.RED_MUSHROOM_BLOCK.arch$registryName().toString(), Blocks.BROWN_MUSHROOM_BLOCK.arch$registryName().toString()),
			new ArrayList<>()
	);
}
