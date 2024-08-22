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

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
#if MC_VER >= MC_1_20_5
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
#endif
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class BlockMapEntityData #if MC_VER < MC_1_20_5 implements EntityDataSerializer<Map<BlockPos, BlockState>> #endif {
	#if MC_VER >= MC_1_20_5
		public static StreamCodec<ByteBuf, Map<BlockPos, BlockState>> BLOCK_MAP_CODEC = new StreamCodec<ByteBuf, Map<BlockPos, BlockState>>() {
			public @NotNull Map<BlockPos, BlockState> decode(ByteBuf byteBuf) {
				int size = VarInt.read(byteBuf);
				Map<BlockPos, BlockState> map = Maps.newHashMapWithExpectedSize(size);

				for (int i = 0; i < size; i++) {
					map.put(FriendlyByteBuf.readBlockPos(byteBuf), Block.stateById(VarInt.read(byteBuf)));
				}
				return map;
			}

			public void encode(ByteBuf byteBuf, Map<BlockPos, BlockState> map) {
				VarInt.write(byteBuf, map.size());
				map.forEach((blockPos, blockState) -> {
					FriendlyByteBuf.writeBlockPos(byteBuf, blockPos);
					VarInt.write(byteBuf, Block.getId(blockState));
				});
			}
		};

		public static final EntityDataSerializer<Map<BlockPos, BlockState>> BLOCK_MAP = EntityDataSerializer.forValueType(BLOCK_MAP_CODEC);
	#else
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
	#endif
}
