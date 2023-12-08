package me.pandamods.fallingtrees.registry;

import com.sun.source.tree.Tree;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.pandamods.fallingtrees.FallingTrees;
import me.pandamods.fallingtrees.entity.TreeEntity;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntityRegistry {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(FallingTrees.MOD_ID, Registry.ENTITY_TYPE_REGISTRY);

	public static final RegistrySupplier<EntityType<TreeEntity>> TREE = ENTITIES.register("tree", () ->
			EntityType.Builder.of(TreeEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).noSave().fireImmune().build("tree"));
}
