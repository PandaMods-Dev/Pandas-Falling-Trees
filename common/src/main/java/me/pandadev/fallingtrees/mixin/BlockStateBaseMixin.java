package me.pandadev.fallingtrees.mixin;

import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.tree.TreeCache;
import me.pandadev.fallingtrees.tree.TreeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
	@Shadow public abstract Block getBlock();

	@Inject(method = "getDestroyProgress", at = @At("RETURN"), cancellable = true)
	public void getDestroyProgress(Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
		if (TreeUtils.isLog(this.getBlock())) {
			TreeCache cache = TreeCache.getOrCreateCache("tree_breaking", pos, level, player);
			if (FallingTrees.getServerConfig().tree_mining_speed_by_log_amount && TreeUtils.shouldTreeFall(player) &&
					!cache.isTreeSizeToBig() && cache.isTree())
				cir.setReturnValue(cir.getReturnValue() /
						((Math.min(FallingTrees.getServerConfig().tree_mining_speed_max_log_limit,
								cache.getLogAmount())-1) *
								FallingTrees.getServerConfig().tree_mining_speed_multiplier + 1));
		}
	}
}
