package me.pandamods.fallingtrees.config.common.tree;

#if MC_VER >= MC_1_20
import net.minecraft.core.registries.BuiltInRegistries;
#else
import net.minecraft.core.Registry;
#endif
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class VerticalTreeConfig extends TreeConfig {
	public Filter filter = new Filter(
			new ArrayList<>(),
			#if MC_VER >= MC_1_20
				List.of(BuiltInRegistries.BLOCK.getKey(Blocks.CACTUS).toString(), BuiltInRegistries.BLOCK.getKey(Blocks.BAMBOO).toString()),
			#else
				List.of(Registry.BLOCK.getKey(Blocks.CACTUS).toString(), Registry.BLOCK.getKey(Blocks.BAMBOO).toString()),
			#endif
			new ArrayList<>()
	);
}
