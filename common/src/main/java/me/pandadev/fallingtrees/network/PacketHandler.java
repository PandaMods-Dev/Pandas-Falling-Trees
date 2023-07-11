package me.pandadev.fallingtrees.network;

import com.google.gson.Gson;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.FallingTreesConfig;
import me.pandadev.fallingtrees.tree.TreeUtils;
import net.fabricmc.api.EnvType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class PacketHandler {
	public static final ResourceLocation CONFIG_PACKET_ID = new ResourceLocation(FallingTrees.MOD_ID, "config_packet");
	public static final ResourceLocation BREAK_TREE_PACKET_ID = new ResourceLocation(FallingTrees.MOD_ID, "break_tree_packet");

	public static void init() {
		if (Platform.getEnv() == EnvType.CLIENT) {
			NetworkManager.registerReceiver(NetworkManager.Side.S2C, CONFIG_PACKET_ID, (buf, context) -> {
				FallingTrees.serverConfig = new Gson().fromJson(new String(buf.readByteArray()), FallingTreesConfig.class);
			});
		}

		NetworkManager.registerReceiver(NetworkManager.Side.C2S, BREAK_TREE_PACKET_ID, (buf, context) -> {
			BlockPos pos = buf.readBlockPos();
			if (buf.readBoolean()) {
				TreeUtils.breakTree(context.getPlayer(), context.getPlayer().level, pos);
			} else {
				BlockState state = context.getPlayer().getLevel().getBlockState(pos);
				context.getPlayer().level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(context.getPlayer(), state));
			}
		});
	}
}
