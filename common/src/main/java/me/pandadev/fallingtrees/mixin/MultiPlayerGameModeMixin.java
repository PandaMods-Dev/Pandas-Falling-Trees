package me.pandadev.fallingtrees.mixin;

import com.sun.source.tree.Tree;
import me.pandadev.fallingtrees.network.BreakTreePacket;
import me.pandadev.fallingtrees.tree.TreeCache;
import me.pandadev.fallingtrees.tree.TreeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
	@Shadow @Final private Minecraft minecraft;

	@Shadow private float destroyProgress;

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
				BreakTreePacket.sendToServer(pos, player);
			}
		}
	}

	@Unique
	boolean isShifting = true;

	@Inject(method = "startDestroyBlock", at = @At("HEAD"))
	public void startDestroyBlock(BlockPos loc, Direction face, CallbackInfoReturnable<Boolean> cir) {
		Player player = this.minecraft.player;
		if (player != null) {
			if (player.isCrouching() != isShifting) {
				isShifting = player.isCrouching();
			}
		}
	}

	@Inject(method = "continueDestroyBlock", at = @At("HEAD"))
	public void continueDestroyBlock(BlockPos posBlock, Direction directionFacing, CallbackInfoReturnable<Boolean> cir) {
		Player player = this.minecraft.player;
		Level level = this.minecraft.level;
		if (level != null && TreeUtils.isLog(level.getBlockState(posBlock).getBlock()) && player != null) {
			if (player.isCrouching() != isShifting) {
				isShifting = player.isCrouching();
				this.destroyProgress = 0;
			}
		}
	}
}
