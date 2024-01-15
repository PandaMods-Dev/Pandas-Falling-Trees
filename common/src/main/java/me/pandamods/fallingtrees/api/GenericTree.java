package me.pandamods.fallingtrees.api;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.config.CommonConfig;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.config.common.TreeConfig;
import me.pandamods.fallingtrees.entity.TreeEntity;
import me.pandamods.pandalib.utils.ClassUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Set;

public abstract class GenericTree extends Tree<TreeConfig> {
	@Override
	public Class<TreeConfig> getConfigClass() {
		return TreeConfig.class;
	}
}
