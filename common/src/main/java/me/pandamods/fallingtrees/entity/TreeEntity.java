package me.pandamods.fallingtrees.entity;

import me.pandamods.fallingtrees.api.TreeType;
import me.pandamods.fallingtrees.registry.EntityRegistry;
import me.pandamods.fallingtrees.utils.BlockMapEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TreeEntity extends Entity {
	public static final EntityDataAccessor<Map<BlockPos, BlockState>> BLOCKS = SynchedEntityData.defineId(TreeEntity.class, BlockMapEntityData.BLOCK_MAP);
	public static final EntityDataAccessor<Integer> HEIGHT = SynchedEntityData.defineId(TreeEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(TreeEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<BlockPos> ORIGIN_POS = SynchedEntityData.defineId(TreeEntity.class, EntityDataSerializers.BLOCK_POS);

	public TreeType treeType;

	public TreeEntity(EntityType<?> entityType, Level level) {
		super(entityType, level);
		this.noCulling = true;
	}

	public static void destroyTree(Set<BlockPos> blockPosList, BlockPos blockPos, LevelAccessor levelAccessor, TreeType treeType) {
		if (levelAccessor instanceof Level level) {
			TreeEntity treeEntity = new TreeEntity(EntityRegistry.TREE.get(), level);
			treeEntity.setPos(blockPos.getCenter().add(0, -.5, 0));
			treeEntity.setData(blockPosList, blockPos, treeType);
			level.addFreshEntity(treeEntity);
		}
	}

	public void setData(Set<BlockPos> blockPosList, BlockPos originBlock, TreeType treeType) {
		this.treeType = treeType;

		int height = 0;

		Map<BlockPos, BlockState> blockPosMap = new HashMap<>();
		for (BlockPos pos : blockPosList) {
			if (pos.getY() > height)
				height = pos.getY() - originBlock.getY();
			blockPosMap.put(pos.immutable().subtract(originBlock), level().getBlockState(pos));
		}
		this.getEntityData().set(ORIGIN_POS, originBlock);
		this.getEntityData().set(BLOCKS, blockPosMap);
		this.getEntityData().set(HEIGHT, height);
//		if (this.getHeight() > 10) {
//			this.getEntityData().set(LIFETIME, (int) (180 * 1.5));
//		}
	}

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(BLOCKS, new HashMap<>());
		this.getEntityData().define(HEIGHT, 0);
		this.getEntityData().define(LIFETIME, 180);
		this.getEntityData().define(ORIGIN_POS, new BlockPos(0, 0, 0));
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

		if (this.tickCount >= getLifeTime()) {
			this.remove(RemovalReason.DISCARDED);
		}
		if (this.tickCount >= getLifeTime() / 2) {

		}
	}

	public Map<BlockPos, BlockState> getBlocks() {
		return this.getEntityData().get(BLOCKS);
	}

	public int getLifeTime() {
		return this.getEntityData().get(LIFETIME);
	}

	public float getHeight() {
		return this.getEntityData().get(HEIGHT);
	}

	public BlockPos getOriginPos() {
		return this.getEntityData().get(ORIGIN_POS);
	}
}
