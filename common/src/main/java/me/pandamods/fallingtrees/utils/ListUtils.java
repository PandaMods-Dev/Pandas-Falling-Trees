package me.pandamods.fallingtrees.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ListUtils {
	public static <K, V> Map<K, V> mapRemoveIf(Map<K, V> map, BiFunction<K, V, Boolean> predicate) {
		Map<K, V> returnMap = new HashMap<>(map);
		map.forEach((k, v) -> {
			if (predicate.apply(k, v))
				returnMap.remove(k);
		});
		return returnMap;
	}
}
