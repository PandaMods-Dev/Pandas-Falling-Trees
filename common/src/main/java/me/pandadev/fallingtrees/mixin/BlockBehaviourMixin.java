package me.pandadev.fallingtrees.mixin;

import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.tree.TreeCache;
import me.pandadev.fallingtrees.tree.TreeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviourMixin {
	@Shadow protected abstract BlockState asState();

	@Inject(method = "getDestroyProgress", at = @At("RETURN"), cancellable = true)
	public void getDestroyProgress(Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
		if (TreeUtils.isLog(this.asState().getBlock())) {
			TreeCache cache = TreeCache.getOrCreateCache("tree_breaking", pos, player.getLevel(), player);
			if (!FallingTrees.serverConfig.tree_mining_speed_by_log_amount)
				return;
			cir.setReturnValue((cir.getReturnValue() / cache.getLogAmount()) *
					FallingTrees.serverConfig.tree_mining_speed_multiplier);
		}
	}
}
