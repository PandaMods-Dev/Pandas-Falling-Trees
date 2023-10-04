package me.pandadev.fallingtrees.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.tree.TreeUtils;
import me.pandadev.fallingtrees.utils.PlayerExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class BreakTreePacket {
	public static void serverReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		BlockPos pos = buf.readBlockPos();
		Player player = context.getPlayer();
		MinecraftServer server = player.getServer();
		if (buf.readBoolean() && server != null) {
			sendToClients(pos, server);
			TreeUtils.breakTree(context.getPlayer(), context.getPlayer().level(), pos);
		} else {
			BlockState state = context.getPlayer().level().getBlockState(pos);
			context.getPlayer().level().gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(context.getPlayer(), state));
		}
	}

	public static void clientReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		if (FallingTrees.getClientConfig().sound_effect) {
			context.getPlayer().level().playSound(context.getPlayer(), buf.readBlockPos(), FallingTrees.TREE_FALL.get(), SoundSource.BLOCKS,
					FallingTrees.getClientConfig().sound_effect_volume, 1);
		}
	}

	public static void sendToServer(BlockPos pos, Player player) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBlockPos(pos);
		buf.writeBoolean(((PlayerExtension) player).shouldTreesFall());

		NetworkManager.sendToServer(PacketHandler.BREAK_TREE_PACKET_ID, buf);
	}

	public static void sendToClients(BlockPos pos, MinecraftServer server) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBlockPos(pos);

		List<ServerPlayer> players = server.getPlayerList().getPlayers();
		for (ServerPlayer player : players) {
			NetworkManager.sendToPlayer(player, PacketHandler.BREAK_TREE_PACKET_ID, buf);
		}
	}
}
