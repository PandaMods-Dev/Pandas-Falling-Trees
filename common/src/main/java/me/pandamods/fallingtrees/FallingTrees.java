/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.fallingtrees;

import com.mojang.logging.LogUtils;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.event.EventHandler;
import me.pandamods.fallingtrees.registry.EntityRegistry;
import me.pandamods.fallingtrees.registry.SoundRegistry;
import me.pandamods.fallingtrees.registry.TreeTypeRegistry;
import me.pandamods.fallingtrees.utils.BlockMapEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class FallingTrees {
    public static final String MOD_ID = "fallingtrees";
	public static final FallingTreesConfig CONFIG = new FallingTreesConfig();
	public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
		TreeTypeRegistry.register();
		SoundRegistry.SOUNDS.register();
		EntityRegistry.ENTITIES.register();
		EventHandler.register();

		EntityDataSerializers.registerSerializer(BlockMapEntityData.BLOCK_MAP);
    }

	public static ResourceLocation ID(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
