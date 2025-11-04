package de.tomalbrc.balloons.util;

import net.minecraft.resources.ResourceLocation;

public interface BalloonHolderEntity {
    void balloons$setBalloon(ResourceLocation id);
    ResourceLocation balloons$getBalloon();
}
