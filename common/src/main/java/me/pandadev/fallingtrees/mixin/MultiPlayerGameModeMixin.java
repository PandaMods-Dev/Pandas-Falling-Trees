package me.pandadev.fallingtrees.mixin;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.pandadev.fallingtrees.network.PacketHandler;
import me.pandadev.fallingtrees.tree.TreeCache;
import me.pandadev.fallingtrees.tree.TreeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
	@Shadow @Final private Minecraft minecraft;

	@Inject(method = "destroyBlock", at = @At("HEAD"))
	public void destroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		Level level = this.minecraft.level;
		Player player = this.minecraft.player;
		if (level != null && player != null) {
			TreeCache cache = TreeCache.getOrCreateCache("tree_breaking", pos, level, player);
			if (cache.isTreeSizeToBig())
				return;
			BlockState state = level.getBlockState(pos);
			if (TreeUtils.isLog(state.getBlock())) {
				FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
				buf.writeBlockPos(pos);

				buf.writeBoolean(TreeUtils.shouldTreeFall(this.minecraft.player));
				NetworkManager.sendToServer(PacketHandler.BREAK_TREE_PACKET_ID, buf);
			}
		}
	}
}
