package me.pandadev.fallingtrees.mixin;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.pandadev.fallingtrees.network.PacketHandler;
import me.pandadev.fallingtrees.tree.TreeCache;
import me.pandadev.fallingtrees.tree.TreeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour {
	public BlockMixin(Properties properties) {
		super(properties);
	}

	@Inject(method = "playerWillDestroy",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V"
			),
			cancellable = true)
	public void blockMine(Level level, BlockPos blockPos, BlockState blockState, Player player, CallbackInfo ci) {
		if (TreeUtils.isLog(blockState.getBlock())) {
			TreeCache cache = TreeCache.getOrCreateCache("tree_breaking", blockPos, player.getLevel(), player);
			if (cache.isTreeSizeToBig())
				return;
			ci.cancel();
			if (level.isClientSide()) {
				FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
				buf.writeBlockPos(blockPos);

				buf.writeBoolean(TreeUtils.shouldTreeFall(player));

				NetworkManager.sendToServer(PacketHandler.BREAK_TREE_PACKET_ID, buf);
			}
		}
	}
}
