package me.pandadev.dynamictrees;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.pandadev.dynamictrees.client.renderer.TreeRenderer;
import me.pandadev.dynamictrees.entity.TreeEntity;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class DynamicTrees {
	public static final String MOD_ID = "dynamictrees";

	// Entity Registry
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(MOD_ID, Registries.ENTITY_TYPE);

	public static final RegistrySupplier<EntityType<TreeEntity>> TREE_ENTITY = ENTITIES.register("tree", () ->
			EntityType.Builder.of(TreeEntity::new, MobCategory.MISC).sized(1f, 1f)
					.fireImmune().build("tree"));

	public static void init() {
		AutoConfig.register(TreesConfig.class, GsonConfigSerializer::new);
		EntityDataSerializers.registerSerializer(DynamicTrees.BLOCK_MAP);

		ClientLifecycleEvent.CLIENT_SETUP.register(DynamicTrees::clientInit);

		ENTITIES.register();
	}

	@Environment(EnvType.CLIENT)
	private static void clientInit(Minecraft minecraft) {
		EntityRendererRegistry.register(TREE_ENTITY, TreeRenderer::new);
	}

	public static final EntityDataSerializer<Map<BlockPos, BlockState>> BLOCK_MAP = new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf buffer, Map<BlockPos, BlockState> value) {
			buffer.writeMap(value, FriendlyByteBuf::writeBlockPos, (friendlyByteBuf, state) -> friendlyByteBuf.writeVarInt(Block.getId(state)));
		}

		@Override
		public Map<BlockPos, BlockState> read(FriendlyByteBuf buffer) {
			return buffer.readMap(FriendlyByteBuf::readBlockPos, buf -> Block.stateById(buf.readVarInt()));
		}

		@Override
		public Map<BlockPos, BlockState> copy(Map<BlockPos, BlockState> value) {
			return new HashMap<>(value);
		}
	};
}