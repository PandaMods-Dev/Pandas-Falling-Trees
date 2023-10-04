package me.pandadev.fallingtrees.mixin;

import me.pandadev.fallingtrees.network.BreakTreePacket;
import me.pandadev.fallingtrees.tree.TreeCache;
import me.pandadev.fallingtrees.utils.PlayerExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.prediction.PredictiveAction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
	@Shadow @Final private Minecraft minecraft;

	@Shadow private float destroyProgress;

	@Shadow private float destroyTicks;

	@Shadow private int destroyDelay;

	@Shadow protected abstract void startPrediction(ClientLevel level, PredictiveAction action);

	@Inject(method = "destroyBlock", at = @At("HEAD"))
	public void destroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		Level level = this.minecraft.level;
		Player player = this.minecraft.player;
		if (level != null && player != null) {
			TreeCache cache = TreeCache.getOrCreateCache("tree_breaking", pos, level, player);
			if (cache == null || cache.isTreeSizeToBig())
				return;
			BreakTreePacket.sendToServer(pos, player);
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
