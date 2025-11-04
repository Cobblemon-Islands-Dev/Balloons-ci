package de.tomalbrc.balloons;

import de.tomalbrc.balloons.component.BalloonToken;
import de.tomalbrc.balloons.component.ModComponents;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BalloonFenceLeashKnot extends BlockAttachedEntity implements PolymerEntity {
    private static final String BALLOON_KEY = "BalloonId";

    ResourceLocation balloonId;

    public BalloonFenceLeashKnot(EntityType<? extends BlockAttachedEntity> entityType, Level level) {
        super(entityType, level);
    }

    public BalloonFenceLeashKnot(Level level, BlockPos blockPos) {
        super(ModEntities.LEASH_KNOT, level);
        this.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public void setBalloonId(ResourceLocation balloonId) {
        this.balloonId = balloonId;
    }

    public ResourceLocation getBalloonId() {
        return this.balloonId;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains(BALLOON_KEY) && balloonId == null) {
            balloonId = ResourceLocation.parse(compoundTag.getString(BALLOON_KEY));
            Balloons.spawnBalloon(this, balloonId);
        }
    }

    @Override
    public void dropItem(@Nullable Entity entity) {
        var configuredBalloon = Balloons.all().get(balloonId);
        var item = configuredBalloon.item();
        item.set(ModComponents.TOKEN, new BalloonToken(configuredBalloon.id(), configuredBalloon.permission(), null));
        spawnAtLocation(item);
        Balloons.despawnBalloon(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (balloonId != null) {
            compoundTag.putString(BALLOON_KEY, balloonId.toString());
        }
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    protected void recalculateBoundingBox() {
        this.setPosRaw(this.pos.getX() + 0.5F, this.pos.getY() + 0.375F, this.pos.getZ() + 0.5F);
        double d = this.getType().getWidth() / 2.0F;
        double e = this.getType().getHeight();
        this.setBoundingBox(new AABB(this.getX() - d, this.getY(), this.getZ() - d, this.getX() + d, this.getY() + e, this.getZ() + d));
    }

    @Override
    public boolean survives() {
        return this.level().getBlockState(this.pos).is(BlockTags.FENCES);
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayer serverPlayer) {
        return EntityType.LEASH_KNOT;
    }

    @Override
    public @NotNull InteractionResult interact(Player player, InteractionHand interactionHand) {
        return InteractionResult.PASS;
    }
}
