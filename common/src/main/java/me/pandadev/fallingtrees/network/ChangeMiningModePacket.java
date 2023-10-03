package me.pandadev.fallingtrees.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.pandadev.fallingtrees.utils.PlayerExtension;
import net.minecraft.network.FriendlyByteBuf;

public class ChangeMiningModePacket {
	public static void serverReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		if (context.getPlayer() instanceof PlayerExtension playerExtension) {
			playerExtension.setMiningOneBlock(buf.readBoolean());
		}
	}

	public static void sendToServer(boolean value) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBoolean(value);

		NetworkManager.sendToServer(PacketHandler.CHANGE_MINING_MODE_PACKET_ID, buf);
	}
}
