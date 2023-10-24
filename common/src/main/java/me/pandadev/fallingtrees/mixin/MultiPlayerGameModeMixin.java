package me.pandadev.fallingtrees.mixin;

import me.pandadev.fallingtrees.network.BreakTreePacket;
import me.pandadev.fallingtrees.tree.TreeCache;
import me.pandadev.fallingtrees.utils.PlayerExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
	@Shadow @Final private Minecraft minecraft;

	@Shadow private float destroyProgress;

	@Inject(
			method = "destroyBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true)
	public void destroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		Level level = this.minecraft.level;
		Player player = this.minecraft.player;
		if (level != null && player != null) {
			BlockState blockState = level.getBlockState(pos);
			blockState.getBlock().playerWillDestroy(level, pos, blockState, this.minecraft.player);
			TreeCache cache = TreeCache.getOrCreateCache("tree_breaking", pos, level, player);
			if (cache == null || cache.isTreeSizeToBig() || !cache.treeType().extraBlockRequirement(cache.getBlocksMap(pos), level))
				return;
			BreakTreePacket.sendToServer(pos, cache.treeType(), player);
			cir.setReturnValue(true);
		}
	}

	@Unique
	boolean miningOneBlock = false;
	@Inject(method = "startDestroyBlock", at = @At("HEAD"))
	public void startDestroyBlock(BlockPos pos, Direction face, CallbackInfoReturnable<Boolean> cir) {
		Player player = this.minecraft.player;
		if (player != null) {
			if (((PlayerExtension) player).isMiningOneBlock() != miningOneBlock) {
				miningOneBlock = ((PlayerExtension) player).isMiningOneBlock();
			}
		}
	}

	@Inject(method = "continueDestroyBlock", at = @At(value = "HEAD"))
	public void continueDestroyBlock(BlockPos pos, Direction directionFacing, CallbackInfoReturnable<Boolean> cir) {
		Player player = this.minecraft.player;
		Level level = this.minecraft.level;
		if (level != null && player != null) {
			TreeCache cache = TreeCache.getOrCreateCache("tree_breaking", pos, level, player);
			if (cache != null && miningOneBlock != ((PlayerExtension) player).isMiningOneBlock()) {
				miningOneBlock = ((PlayerExtension) player).isMiningOneBlock();
				this.destroyProgress = 0;
			}
		}
	}
}
