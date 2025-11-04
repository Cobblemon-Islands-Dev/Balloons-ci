package de.tomalbrc.balloons.mixin;

import de.tomalbrc.balloons.Balloons;
import de.tomalbrc.balloons.util.BalloonHolderEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeashFenceKnotEntity.class)
public abstract class LeashKnotMixin extends Entity implements BalloonHolderEntity {
    @Unique
    private static final String BALLOON_KEY = "BalloonId";

    @Unique private ResourceLocation balloons$Id;

    public LeashKnotMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void balloons$onSave(CompoundTag compoundTag, CallbackInfo ci) {
        if (balloons$Id != null) compoundTag.putString(BALLOON_KEY, balloons$Id.toString());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void balloons$onLoad(CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.contains(BALLOON_KEY)) {
            balloons$Id = ResourceLocation.parse(compoundTag.getString(BALLOON_KEY));
        }
    }

    @Inject(method = "interact", at = @At("HEAD"))
    private void balloons$onInteract(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (balloons$Id != null)  {
            Balloons.despawnBalloon(this);
            discard();
        }
    }

    @Override
    public ResourceLocation balloons$getBalloon() {
        return balloons$Id;
    }

    @Override
    public void balloons$setBalloon(ResourceLocation id) {
        balloons$Id = id;
    }

}
