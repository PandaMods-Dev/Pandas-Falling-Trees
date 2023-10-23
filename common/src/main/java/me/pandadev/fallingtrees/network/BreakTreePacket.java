package me.pandadev.fallingtrees.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.pandadev.fallingtrees.tree.TreeType;
import me.pandadev.fallingtrees.tree.TreeTypeRegistry;
import me.pandadev.fallingtrees.tree.TreeUtils;
import me.pandadev.fallingtrees.utils.PlayerExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
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
			if (TreeUtils.breakTree(context.getPlayer(), context.getPlayer().level(), pos)) {
				sendToClients(pos, buf.readUtf(), server);
			}
		} else {
			BlockState state = context.getPlayer().level().getBlockState(pos);
			context.getPlayer().level().gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(context.getPlayer(), state));
		}
	}

	public static void clientReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		TreeTypeRegistry.getTreeType(new ResourceLocation(buf.readUtf())).ifPresent(treeType ->
				treeType.onFall(context.getPlayer(), buf.readBlockPos(), context.getPlayer().level()));
	}

	public static void sendToServer(BlockPos pos, TreeType treeType, Player player) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBlockPos(pos);
		buf.writeBoolean(((PlayerExtension) player).shouldTreesFall());
		buf.writeUtf(TreeTypeRegistry.getTreeTypeName(treeType).toString());

		NetworkManager.sendToServer(PacketHandler.BREAK_TREE_PACKET_ID, buf);
	}

	public static void sendToClients(BlockPos pos, String treeName, MinecraftServer server) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeUtf(treeName);
		buf.writeBlockPos(pos);

		List<ServerPlayer> players = server.getPlayerList().getPlayers();
		for (ServerPlayer player : players) {
			NetworkManager.sendToPlayer(player, PacketHandler.BREAK_TREE_PACKET_ID, buf);
		}
	}
}
