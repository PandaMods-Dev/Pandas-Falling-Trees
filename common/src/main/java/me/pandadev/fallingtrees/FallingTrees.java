package me.pandadev.fallingtrees;

import com.google.gson.Gson;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.netty.buffer.Unpooled;
import me.pandadev.fallingtrees.client.renderer.TreeRenderer;
import me.pandadev.fallingtrees.entity.TreeEntity;
import me.pandadev.fallingtrees.network.PacketHandler;
import me.pandadev.fallingtrees.registries.Keybindings;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class FallingTrees {
	public static final String MOD_ID = "fallingtrees";
	public static ConfigHolder<FallingTreesConfig> configHolder;
	public static FallingTreesConfig serverConfig;

	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(MOD_ID, Registries.SOUND_EVENT);

	public static final RegistrySupplier<SoundEvent> TREE_FALL = SOUNDS.register("tree_fall", () ->
			SoundEvent.createFixedRangeEvent(new ResourceLocation(MOD_ID, "tree_fall"), 16));

	// Entity Registry
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(MOD_ID, Registries.ENTITY_TYPE);

	public static final RegistrySupplier<EntityType<TreeEntity>> TREE_ENTITY = ENTITIES.register("tree", () ->
			EntityType.Builder.of(TreeEntity::new, MobCategory.MISC).sized(1f, 1f)
					.fireImmune().build("tree"));

	public static void init() {
		AutoConfig.register(FallingTreesConfig.class, GsonConfigSerializer::new);
		configHolder = AutoConfig.getConfigHolder(FallingTreesConfig.class);
		serverConfig = configHolder.getConfig();

		SOUNDS.register();
		ENTITIES.register();
		if (Platform.getEnv() == EnvType.CLIENT) {
			clientInit();
			Keybindings.init();
		}

		PlayerEvent.PLAYER_JOIN.register(FallingTrees::onPlayerJoin);

		PacketHandler.init();

		EntityDataSerializers.registerSerializer(FallingTrees.BLOCK_MAP);
	}

	private static void onPlayerJoin(ServerPlayer serverPlayer) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeByteArray(new Gson().toJson(configHolder.getConfig()).getBytes());
		NetworkManager.sendToPlayer(serverPlayer, PacketHandler.CONFIG_PACKET_ID, buf);
	}

	public static void clientInit() {
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