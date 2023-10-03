package me.pandadev.fallingtrees.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.pandadev.fallingtrees.tree.TreeUtils;
import me.pandadev.fallingtrees.utils.PlayerExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class BreakTreePacket {
	public static void serverReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		BlockPos pos = buf.readBlockPos();
		if (buf.readBoolean()) {
			sendToClients(pos);
			TreeUtils.breakTree(context.getPlayer(), context.getPlayer().level(), pos);
		} else {
			BlockState state = context.getPlayer().level().getBlockState(pos);
			context.getPlayer().level().gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(context.getPlayer(), state));
		}
	}

	public static void clientReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		System.out.println("client: " + context.getPlayer().getDisplayName());
	}

	public static void sendToServer(BlockPos pos, Player player) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBlockPos(pos);
		buf.writeBoolean(((PlayerExtension) player).shouldTreesFall());

		NetworkManager.sendToServer(PacketHandler.BREAK_TREE_PACKET_ID, buf);
	}

	public static void sendToClients(BlockPos pos) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
//		buf.writeBlockPos(pos);
//		if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getServer() != null) {
//			List<ServerPlayer> players = Minecraft.getInstance().player.getServer().getPlayerList().getPlayers();
//			for (ServerPlayer player : players) {
//				System.out.println("server: " + player.getDisplayName());
//				NetworkManager.sendToPlayer(player, PacketHandler.BREAK_TREE_PACKET_ID, buf);
//			}
//		}
	}
}
