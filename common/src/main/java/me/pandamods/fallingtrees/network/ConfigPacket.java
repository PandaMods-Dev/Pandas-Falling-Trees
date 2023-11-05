package me.pandamods.fallingtrees.network;

import com.google.gson.Gson;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.config.CommonConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ConfigPacket {
	public static void clientReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		FallingTrees.setCommonConfig(new Gson().fromJson(new String(buf.readByteArray()), CommonConfig.class));
	}

	public static void sendToPlayer(ServerPlayer player) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeByteArray(new Gson().toJson(FallingTrees.getCommonConfig()).getBytes());
		NetworkManager.sendToPlayer(player, PacketHandler.CONFIG_PACKET_ID, buf);
	}
}
