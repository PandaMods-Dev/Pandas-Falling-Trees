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

package me.pandamods.fallingtrees.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BlockMapEntityData implements EntityDataSerializer<Map<BlockPos, BlockState>> {
	public static final EntityDataSerializer<Map<BlockPos, BlockState>> BLOCK_MAP = new BlockMapEntityData();

	@Override
	public void write(FriendlyByteBuf buffer, Map<BlockPos, BlockState> value) {
		buffer.writeMap(value, FriendlyByteBuf::writeBlockPos, (friendlyByteBuf, state) -> friendlyByteBuf.writeVarInt(Block.getId(state)));
	}

	@Override
	public @NotNull Map<BlockPos, BlockState> read(FriendlyByteBuf buffer) {
		return buffer.readMap(FriendlyByteBuf::readBlockPos, buf -> Block.stateById(buf.readVarInt()));
	}

	@Override
	public @NotNull Map<BlockPos, BlockState> copy(Map<BlockPos, BlockState> value) {
		return new HashMap<>(value);
	}
}
