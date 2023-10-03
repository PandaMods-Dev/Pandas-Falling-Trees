package me.pandadev.fallingtrees.mixin;

import me.pandadev.fallingtrees.FallingTrees;
import me.pandadev.fallingtrees.utils.PlayerExtension;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerExtension {
	@Unique
	private static final EntityDataAccessor<Boolean> ONLY_MINE_ONE_BLOCK_DATA = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "defineSynchedData", at = @At("RETURN"))
	public void defineSynchedData(CallbackInfo ci) {
		this.entityData.define(ONLY_MINE_ONE_BLOCK_DATA, false);
	}

	@Override
	public void setMiningOneBlock(boolean value) {
		this.entityData.set(ONLY_MINE_ONE_BLOCK_DATA, value, true);
	}

	@Override
	public boolean isMiningOneBlock() {
		return this.entityData.get(ONLY_MINE_ONE_BLOCK_DATA);
	}

	@Override
	public boolean shouldTreesFall() {
		if (FallingTrees.getServerConfig().tree_limit.only_fall_on_tool_use) {
			return this.getMainHandItem().getItem() instanceof AxeItem &&
					!(FallingTrees.getServerConfig().allow_one_block_mining  && isMiningOneBlock());
		}
		return !(FallingTrees.getServerConfig().allow_one_block_mining && isMiningOneBlock());
	}
}
