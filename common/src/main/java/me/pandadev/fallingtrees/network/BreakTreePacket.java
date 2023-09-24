package me.pandadev.fallingtrees.network;

import dev.architectury.networking.NetworkManager;
import me.pandadev.fallingtrees.tree.TreeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class BreakTreePacket {
	public static void serverReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		BlockPos pos = buf.readBlockPos();
		if (buf.readBoolean()) {
			TreeUtils.breakTree(context.getPlayer(), context.getPlayer().level(), pos);
		} else {
			BlockState state = context.getPlayer().level().getBlockState(pos);
			context.getPlayer().level().gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(context.getPlayer(), state));
		}
	}
}
