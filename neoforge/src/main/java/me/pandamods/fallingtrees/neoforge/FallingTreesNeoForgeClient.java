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

package me.pandamods.fallingtrees.neoforge;

import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.FallingTreesClient;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(FallingTrees.MOD_ID)
public class FallingTreesNeoForgeClient {
	public static void clientInit() {
		FallingTreesClient.init();
		ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (minecraft, screen) ->
				PandaLibConfig.getConfigScreen(screen, FallingTrees.MOD_ID));
	}
}
