package me.pandadev.fallingtrees;

import dev.architectury.event.events.common.PlayerEvent;
import me.pandadev.fallingtrees.network.ConfigPacket;
import net.minecraft.server.level.ServerPlayer;

public class EventHandler {
	public static void init() {
		PlayerEvent.PLAYER_JOIN.register(EventHandler::onPlayerJoin);
	}

	private static void onPlayerJoin(ServerPlayer serverPlayer) {
		ConfigPacket.sendToPlayer(serverPlayer);
	}
}
