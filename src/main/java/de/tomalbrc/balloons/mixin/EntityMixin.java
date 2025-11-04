package de.tomalbrc.balloons.mixin;

import de.tomalbrc.balloons.Balloons;
import de.tomalbrc.balloons.component.BalloonToken;
import de.tomalbrc.balloons.component.ModComponents;
import de.tomalbrc.balloons.util.BalloonHolderEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow @Nullable public abstract ItemEntity spawnAtLocation(ItemStack itemStack);

    @Shadow @Nullable private Entity.RemovalReason removalReason;

    @Inject(method = "setRemoved", at = @At("HEAD"))
    private void balloons$onRemoved(Entity.RemovalReason removalReason, CallbackInfo ci) {
        if (this.removalReason == null && removalReason.shouldDestroy() && this instanceof BalloonHolderEntity balloonHolderEntity) {
            var configuredBalloon = Balloons.all().get(balloonHolderEntity.balloons$getBalloon());
            var item = configuredBalloon.item();
            item.set(ModComponents.TOKEN, new BalloonToken(configuredBalloon.id(), configuredBalloon.permission(), null));
            spawnAtLocation(item);
            Balloons.despawnBalloon((Entity)(Object) this);
        }
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void balloons$onInitLoad(CompoundTag compoundTag, CallbackInfo ci) {
        if (this instanceof BalloonHolderEntity balloonHolderEntity) {
            var configuredBalloon = Balloons.all().get(balloonHolderEntity.balloons$getBalloon());
            Balloons.spawnBalloon((Entity)(Object) this, configuredBalloon.id());
        }
    }
}
