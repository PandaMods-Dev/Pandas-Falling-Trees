package me.pandamods.fallingtrees.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.pandamods.fallingtrees.FallingTrees;
#if MC_VER >= MC_1_20
import net.minecraft.core.registries.Registries;
#else
import net.minecraft.core.Registry;
#endif
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundRegistry {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(FallingTrees.MOD_ID,
			#if MC_VER >= MC_1_20 Registries.SOUND_EVENT #else Registry.SOUND_EVENT_REGISTRY #endif);

	public static final RegistrySupplier<SoundEvent> TREE_FALL = SOUNDS.register("tree_fall", () ->
			createFixedRangeEvent(FallingTrees.ID("tree_fall"), 16));

	public static final RegistrySupplier<SoundEvent> TREE_IMPACT = SOUNDS.register("tree_impact", () ->
			createFixedRangeEvent(FallingTrees.ID("tree_impact"), 16));

	private static SoundEvent createFixedRangeEvent(ResourceLocation resourceLocation, int range) {
		#if MC_VER >= MC_1_20
			return SoundEvent.createFixedRangeEvent(resourceLocation, range);
		#else
			return new SoundEvent(resourceLocation, range);
		#endif
	}
}
