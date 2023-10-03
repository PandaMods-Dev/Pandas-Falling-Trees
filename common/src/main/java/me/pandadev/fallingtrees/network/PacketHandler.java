package me.pandadev.fallingtrees.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import me.pandadev.fallingtrees.FallingTrees;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;

public class PacketHandler {
	public static final ResourceLocation CONFIG_PACKET_ID = new ResourceLocation(FallingTrees.MOD_ID, "config_packet");
	public static final ResourceLocation BREAK_TREE_PACKET_ID = new ResourceLocation(FallingTrees.MOD_ID, "break_tree_packet");
	public static final ResourceLocation CHANGE_MINING_MODE_PACKET_ID = new ResourceLocation(FallingTrees.MOD_ID, "change_mining_mode_packet");

	public static void init() {
		if (Platform.getEnv() == EnvType.CLIENT) {
			NetworkManager.registerReceiver(NetworkManager.serverToClient(), CONFIG_PACKET_ID, ConfigPacket::clientReceiver);
			NetworkManager.registerReceiver(NetworkManager.clientToServer(), BREAK_TREE_PACKET_ID, BreakTreePacket::clientReceiver);
		}

		NetworkManager.registerReceiver(NetworkManager.clientToServer(), BREAK_TREE_PACKET_ID, BreakTreePacket::serverReceiver);
		NetworkManager.registerReceiver(NetworkManager.clientToServer(), CHANGE_MINING_MODE_PACKET_ID, ChangeMiningModePacket::serverReceiver);
	}
}
