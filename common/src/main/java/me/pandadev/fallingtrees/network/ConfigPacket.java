package me.pandadev.fallingtrees.network;

import com.google.gson.Gson;
import dev.architectury.networking.NetworkManager;
import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.config.ServerConfig;
import net.minecraft.network.FriendlyByteBuf;

public class ConfigPacket {

	public static void clientReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		FallingTrees.setServerConfig(new Gson().fromJson(new String(buf.readByteArray()), ServerConfig.class));
	}
}
