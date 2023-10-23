package me.pandadev.fallingtrees.tree;

import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record TreeCache(Vector3i pos, List<BlockPos> blocks, BlockGetter level, TreeType treeType) {
	public static Map<String, Map<Integer, TreeCache>> CACHES = new HashMap<>();

	public static Map<Integer, TreeCache> getCaches(String name) {
		if (!CACHES.containsKey(name)) {
			CACHES.put(name, new HashMap<>());
		}
		return CACHES.get(name);
	}

	public int getLogAmount() {
		int amount = 0;
		for (BlockPos treeBlockPos : blocks) {
			if (treeType.blockChecker(treeBlockPos, level)) amount++;
		}
		return amount;
	}

	public boolean isTreeSizeToBig() {
		if (FallingTrees.getServerConfig().tree_limit.tree_limit_method.equals(ServerConfig.TreeLimit.LimitMethodEnum.BLOCKS)) {
			return blocks.size() > FallingTrees.getServerConfig().tree_limit.tree_size_limit;
		}
		return blocks.stream().filter(blockPos ->
				treeType.blockChecker(blockPos, level)).count() > FallingTrees.getServerConfig().tree_limit.tree_size_limit;
	}

	public static TreeCache getOrCreateCache(String cacheName, BlockPos pos, BlockGetter level, Player player) {
		if (getCaches(cacheName).containsKey(player.getId()) && getCaches(cacheName).get(player.getId()).pos.equals(pos.getX(), pos.getY(), pos.getZ())) {
			return getCaches(cacheName).get(player.getId());
		}
		return TreeCache.createCache(cacheName, pos, level, player);
	}

	public static TreeCache createCache(String cacheName, BlockPos pos, BlockGetter level, Player player) {
		Optional<TreeType> treeType = TreeTypeRegistry.getTreeType(pos, level);
		if (treeType.isEmpty())
			return null;
		List<BlockPos> blocks = treeType.get().blockDetectionAlgorithm(pos, level);
		if (blocks.size() <= 1)
			return null;

		TreeCache cache = new TreeCache(new Vector3i(pos.getX(), pos.getY(), pos.getZ()), blocks, level, treeType.get());
		getCaches(cacheName).put(player.getId(), cache);
		return cache;
	}

	public Map<BlockPos, BlockState> getBlocksMap(BlockPos pos) {
		Map<BlockPos, BlockState> treeBlocks = new HashMap<>();
		for (BlockPos treePos : this.blocks()) {
			BlockState state = level.getBlockState(treePos);
			treeBlocks.put(treePos.subtract(pos), state);
		}
		return treeBlocks;
	}
}
