package me.pandamods.fallingtrees.entity;

import me.pandamods.fallingtrees.api.TreeRegistry;
import me.pandamods.fallingtrees.api.TreeType;
import me.pandamods.fallingtrees.config.FallingTreesConfig;
import me.pandamods.fallingtrees.registry.EntityRegistry;
import me.pandamods.fallingtrees.utils.BlockMapEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class TreeEntity extends Entity {
	public static final EntityDataAccessor<Map<BlockPos, BlockState>> BLOCKS = SynchedEntityData.defineId(TreeEntity.class, BlockMapEntityData.BLOCK_MAP);
	public static final EntityDataAccessor<Integer> HEIGHT = SynchedEntityData.defineId(TreeEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<BlockPos> ORIGIN_POS = SynchedEntityData.defineId(TreeEntity.class, EntityDataSerializers.BLOCK_POS);
	public static final EntityDataAccessor<ItemStack> USED_TOOL = SynchedEntityData.defineId(TreeEntity.class, EntityDataSerializers.ITEM_STACK);
	public static final EntityDataAccessor<Direction> FALL_DIRECTION = SynchedEntityData.defineId(TreeEntity.class, EntityDataSerializers.DIRECTION);
	public static final EntityDataAccessor<String> TREE_TYPE_LOCATION = SynchedEntityData.defineId(TreeEntity.class, EntityDataSerializers.STRING);

	public Entity owner = null;
	public TreeType treeType = null;

	public TreeEntity(EntityType<?> entityType, Level level) {
		super(entityType, level);
		this.noCulling = true;
	}

	public static void destroyTree(Set<BlockPos> blockPosList, BlockPos blockPos, LevelAccessor levelAccessor, TreeType treeType, Player player) {
		if (levelAccessor instanceof Level level) {
			TreeEntity treeEntity = new TreeEntity(EntityRegistry.TREE.get(), level);
			treeEntity.setPos(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()).add(.5, 0, .5));
			treeEntity.setData(blockPosList, blockPos, treeType, player, player.getItemBySlot(EquipmentSlot.MAINHAND));
			level.addFreshEntity(treeEntity);

			for (BlockPos pos : blockPosList) {
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
			}
			for (Map.Entry<BlockPos, BlockState> entry : treeEntity.getBlocks().entrySet()) {
				level.sendBlockUpdated(entry.getKey().offset(blockPos), entry.getValue(), Blocks.AIR.defaultBlockState(), 3);
			}
		}
	}

	public void setData(Set<BlockPos> blockPosList, BlockPos originBlock, TreeType treeType, Entity owner, ItemStack itemStack) {
		this.owner = owner;
		this.treeType = treeType;

		int height = 0;

		Map<BlockPos, BlockState> blockPosMap = new HashMap<>();
		for (BlockPos pos : blockPosList) {
			if (pos.getY() > height)
				height = pos.getY() - originBlock.getY();
			blockPosMap.put(pos.immutable().subtract(originBlock), level.getBlockState(pos));
		}
		this.getEntityData().set(ORIGIN_POS, originBlock);
		this.getEntityData().set(BLOCKS, blockPosMap);
		this.getEntityData().set(HEIGHT, height);
		this.getEntityData().set(USED_TOOL, itemStack);
		ResourceLocation treeTypeLocation = TreeRegistry.getTreeTypeLocation(treeType);
		if (treeTypeLocation != null)
			this.getEntityData().set(TREE_TYPE_LOCATION, treeTypeLocation.toString());
		this.getEntityData().set(FALL_DIRECTION, Direction.fromYRot(
				-Math.toDegrees(Math.atan2(owner.getX() - originBlock.getX(), owner.getZ() - originBlock.getZ()))
		).getOpposite());
	}

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(BLOCKS, new HashMap<>());
		this.getEntityData().define(HEIGHT, 0);
		this.getEntityData().define(ORIGIN_POS, new BlockPos(0, 0, 0));
		this.getEntityData().define(USED_TOOL, ItemStack.EMPTY);
		this.getEntityData().define(FALL_DIRECTION, Direction.NORTH);
		this.getEntityData().define(TREE_TYPE_LOCATION, "");
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	public void tick() {
		super.tick();

		if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
		}
		this.move(MoverType.SELF, this.getDeltaMovement());
		if (this.onGround) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1, -0.5, 1));
		}

		this.getTreeType().entityTick(this);
	}

	public Map<BlockPos, BlockState> getBlocks() {
		return this.getEntityData().get(BLOCKS);
	}

	public int getMaxLifeTimeTick() {
		return (int) (FallingTreesConfig.getCommonConfig().treeLifeLength * 20);
	}

	public float getLifetime(float partialTick) {
		return (this.tickCount + partialTick) / 20;
	}

	public boolean isLarge() {
		return this.getHeight() > 15;
	}

	public float getHeight() {
		return this.getEntityData().get(HEIGHT);
	}

	public BlockPos getOriginPos() {
		return this.getEntityData().get(ORIGIN_POS);
	}

	public ItemStack getUsedTool() {
		return this.getEntityData().get(USED_TOOL);
	}

	public @NotNull Direction getDirection() {
		return this.getEntityData().get(FALL_DIRECTION);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}

	public TreeType getTreeType() {
		Optional<TreeType> treeTypeOptional = TreeRegistry.getTreeType(new ResourceLocation(this.getEntityData().get(TREE_TYPE_LOCATION)));
		return treeTypeOptional.orElse(null);
	}
}
