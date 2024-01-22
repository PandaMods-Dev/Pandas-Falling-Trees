package me.pandamods.fallingtrees.mixin;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.api.Tree;
import me.pandamods.fallingtrees.api.TreeData;
import me.pandamods.fallingtrees.api.TreeDataBuilder;
import me.pandamods.fallingtrees.api.TreeRegistry;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.utils.TreeCache;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.joml.Math;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class BlockStateMixin {
	@Inject(method = "getDestroyProgress", at = @At("RETURN"), cancellable = true)
	public void getDestroyProgress(Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
		if (FallingTreesConfig.getCommonConfig().dynamicMiningSpeed.disable) return;
		TreeRegistry.getTree(level.getBlockState(pos)).ifPresent(tree -> {
			TreeData treeData = TreeCache.get(player, pos, () -> tree.getTreeData(new TreeDataBuilder(), pos, level));
			if (!treeData.shouldFall()) return;
			cir.setReturnValue(cir.getReturnValueF() * Math.max(
					FallingTreesConfig.getCommonConfig().dynamicMiningSpeed.minimumSpeed,
					treeData.miningSpeedMultiply()
			));
		});
	}
}
