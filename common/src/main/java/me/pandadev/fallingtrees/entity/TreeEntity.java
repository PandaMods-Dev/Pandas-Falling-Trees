package me.pandadev.fallingtrees.entity;

import me.pandadev.fallingtrees.FallingTrees;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class TreeEntity extends Entity {
	public static final EntityDataAccessor<Float> ROTATION = SynchedEntityData.defineId(TreeEntity.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Integer> LIFE_TIME = SynchedEntityData.defineId(TreeEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Map<BlockPos, BlockState>> BLOCKS = SynchedEntityData.defineId(TreeEntity.class, FallingTrees.BLOCK_MAP);

	public ItemStack usedItem = ItemStack.EMPTY;
	public Entity owner = null;


	public TreeEntity(EntityType<?> entityType, Level level) {
		super(entityType, level);
		this.noCulling = true;
	}

	public TreeEntity setBlocks(Map<BlockPos, BlockState> blocks) {
		this.getEntityData().set(BLOCKS, blocks);
		this.getEntityData().set(LIFE_TIME, 20*5);
		return this;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(BLOCKS, new HashMap<>());
		this.entityData.define(ROTATION, 0f);
		this.entityData.define(LIFE_TIME, 20);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {
		if (compoundTag.contains("life_time"))
			this.getEntityData().set(LIFE_TIME, compoundTag.getInt("life_time"));
		if (compoundTag.contains("rotation"))
			this.getEntityData().set(ROTATION, compoundTag.getFloat("rotation"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {
		compoundTag.putInt("life_time", this.getEntityData().get(LIFE_TIME));
		compoundTag.putFloat("rotation", this.getEntityData().get(ROTATION));
	}

	@Override
	public void tick() {
		super.tick();
		if (decreesLifeTime() <= 0) {
			this.remove(RemovalReason.DISCARDED);
			for (BlockState state : this.getEntityData().get(BLOCKS).values()) {
				Block.dropResources(state, this.level(), new BlockPos(this.getBlockX(), this.getBlockY(), this.getBlockZ()), null, owner, usedItem);
			}
		}
	}

	public void setRotationY(float rotationY) {
		this.entityData.set(ROTATION, rotationY);
	}

	public int decreesLifeTime() {
		this.getEntityData().set(LIFE_TIME, getLifeTime()-1);
		return getLifeTime()-1;
	}

	public int getLifeTime() {
		return this.getEntityData().get(LIFE_TIME);
	}
}
