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

import net.minecraft.core.BlockPos;

import java.util.Set;

public record TreeData(
		Set<BlockPos> blocks,
		float miningSpeedMultiply,
		boolean shouldFall,
		int toolDamage,
		float foodExhaustionMultiply,
		int awardedBlocks
) {
}
