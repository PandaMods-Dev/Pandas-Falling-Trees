package me.pandamods.fallingtrees.network;

import com.google.gson.Gson;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.config.ClientConfig;
import me.pandamods.fallingtrees.config.CommonConfig;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ConfigPacket {
	public static void clientReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		FallingTrees.CONFIG.setCommonConfig(new Gson().fromJson(new String(buf.readByteArray()), CommonConfig.class));
	}

	public static void sendToPlayer(ServerPlayer player) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeByteArray(new Gson().toJson(FallingTreesConfig.getCommonConfig()).getBytes());
		NetworkManager.sendToPlayer(player, PacketHandler.CONFIG_PACKET_ID, buf);
	}

	public static void serverReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		ClientConfig clientConfig = new Gson().fromJson(new String(buf.readByteArray()), ClientConfig.class);
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("invertCrouchMining", clientConfig.invertCrouchMining);
		context.getPlayer().getEntityData().set(FallingTrees.PLAYER_CLIENT_CONFIG, tag);
	}

	public static void sendToServer() {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeByteArray(new Gson().toJson(FallingTreesConfig.getClientConfig()).getBytes());
		NetworkManager.sendToServer(PacketHandler.CONFIG_PACKET_ID, buf);
	}

	public static CompoundTag getClientConfig(Player player) {
		return player.getEntityData().get(FallingTrees.PLAYER_CLIENT_CONFIG);
	}
}
