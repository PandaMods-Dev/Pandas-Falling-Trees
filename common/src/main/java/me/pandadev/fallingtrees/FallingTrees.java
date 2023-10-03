package me.pandadev.fallingtrees;

import dev.architectury.platform.Platform;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.pandadev.fallingtrees.client.ClientEventHandler;
import me.pandadev.fallingtrees.client.renderer.TreeRenderer;
import me.pandadev.fallingtrees.config.ClientConfig;
import me.pandadev.fallingtrees.config.ModConfig;
import me.pandadev.fallingtrees.config.ServerConfig;
import me.pandadev.fallingtrees.entity.TreeEntity;
import me.pandadev.fallingtrees.network.PacketHandler;
import me.pandadev.fallingtrees.registries.Keybindings;
import me.pandadev.fallingtrees.registries.TreeTypes;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class FallingTrees {
	public static final String MOD_ID = "fallingtrees";
	private static ConfigHolder<ModConfig> configHolder;
	private static ServerConfig serverConfig;

	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(MOD_ID, Registries.SOUND_EVENT);

	public static final RegistrySupplier<SoundEvent> TREE_FALL = SOUNDS.register("tree_fall", () ->
			SoundEvent.createFixedRangeEvent(new ResourceLocation(MOD_ID, "tree_fall"), 16));

	// Entity Registry
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(MOD_ID, Registries.ENTITY_TYPE);

	public static final RegistrySupplier<EntityType<TreeEntity>> TREE_ENTITY = ENTITIES.register("tree", () ->
			EntityType.Builder.of(TreeEntity::new, MobCategory.MISC).sized(1f, 1f)
					.fireImmune().build("tree"));

	public static void init() {
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		configHolder = AutoConfig.getConfigHolder(ModConfig.class);

		SOUNDS.register();
		ENTITIES.register();
		if (Platform.getEnv() == EnvType.CLIENT) {
			clientInit();
			Keybindings.init();
		}

		TreeTypes.init();
		PacketHandler.init();
		EventHandler.init();

		EntityDataSerializers.registerSerializer(FallingTrees.BLOCK_MAP);
	}

	@Environment(EnvType.CLIENT)
	public static void clientInit() {
		EntityRendererRegistry.register(TREE_ENTITY, TreeRenderer::new);

		ClientEventHandler.init();
	}

	public static ServerConfig getServerConfig() {
		if (FallingTrees.serverConfig != null)
			return FallingTrees.serverConfig;
		return configHolder.getConfig().server;
	}

	public static void setServerConfig(ServerConfig serverConfig) {
		FallingTrees.serverConfig = serverConfig;
	}

	public static ClientConfig getClientConfig() {
		return configHolder.getConfig().client;
	}

	public static ConfigHolder<ModConfig> getConfigHolder() {
		return configHolder;
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