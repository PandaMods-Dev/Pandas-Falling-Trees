package me.pandamods.fallingtrees.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.entity.TreeEntity;
#if MC_VER >= MC_1_20
import net.minecraft.core.registries.Registries;
#else
import net.minecraft.core.Registry;
#endif
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntityRegistry {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(FallingTrees.MOD_ID,
			#if MC_VER >= MC_1_20 Registries.ENTITY_TYPE #else Registry.ENTITY_TYPE_REGISTRY #endif);

	public static final RegistrySupplier<EntityType<TreeEntity>> TREE = ENTITIES.register("tree", () ->
			EntityType.Builder.of(TreeEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).noSave().fireImmune().build("tree"));
}
