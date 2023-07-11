package me.pandadev.fallingtrees.tree;

import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.FallingTreesConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record TreeCache(Vector3i pos, List<BlockPos> treeBlocks, Level level, boolean shouldTreeFall) {
	public static Map<String, Map<Integer, TreeCache>> CACHES = new HashMap<>();

	public static Map<Integer, TreeCache> getCaches(String name) {
		if (!CACHES.containsKey(name)) {
			CACHES.put(name, new HashMap<>());
		}
		return CACHES.get(name);
	}

	public int getLogAmount() {
		int amount = 0;
		for (BlockPos treeBlock : treeBlocks) {
			if (TreeUtils.isLog(level.getBlockState(treeBlock).getBlock())) amount++;
		}
		return amount;
	}

	public boolean isTreeSizeToBig() {
		if (FallingTrees.serverConfig.tree_limit.tree_limit_method.equals(FallingTreesConfig.TreeLimit.LimitMethodEnum.BLOCKS)) {
			return treeBlocks.size() > FallingTrees.serverConfig.tree_limit.tree_size_limit;
		}
		return treeBlocks.stream().filter(blockPos ->
				TreeUtils.isLog(level.getBlockState(blockPos).getBlock())).count() > FallingTrees.serverConfig.tree_limit.tree_size_limit;
	}

	public static TreeCache getOrCreateCache(String cacheName, BlockPos pos, Level level, Player player) {
		if (getCaches(cacheName).containsKey(player.getId()) && getCaches(cacheName).get(player.getId()).pos.equals(pos.getX(), pos.getY(), pos.getZ())) {
			return getCaches(cacheName).get(player.getId());
		}
		return TreeCache.createCache(cacheName, pos, level, player);
	}

	public static TreeCache createCache(String cacheName, BlockPos pos, Level level, Player player) {
		TreeCache cache = new TreeCache(new Vector3i(pos.getX(), pos.getY(), pos.getZ()), TreeUtils.getTreeBlocks(pos, level), level,
				TreeUtils.shouldTreeFall(pos, level, player));
		getCaches(cacheName).put(player.getId(), cache);
		return cache;
	}
}
