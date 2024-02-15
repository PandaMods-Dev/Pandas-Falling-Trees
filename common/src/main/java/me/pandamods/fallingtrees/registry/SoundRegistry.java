package me.pandamods.fallingtrees.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.pandamods.fallingtrees.FallingTrees;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundRegistry {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(FallingTrees.MOD_ID, Registry.SOUND_EVENT_REGISTRY);

	public static final RegistrySupplier<SoundEvent> TREE_FALL = SOUNDS.register("tree_fall", () ->
			new SoundEvent(new ResourceLocation(FallingTrees.MOD_ID, "tree_fall"), 16));

	public static final RegistrySupplier<SoundEvent> TREE_IMPACT = SOUNDS.register("tree_impact", () ->
			new SoundEvent(new ResourceLocation(FallingTrees.MOD_ID, "tree_impact"), 16));
}
