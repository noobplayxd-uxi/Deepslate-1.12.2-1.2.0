package com.yourname.deepslateexpansion.mixins.minecraft.world;

import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(WorldProvider.class)
public abstract class WorldProviderMixin {

    // Same constants (can be kept in sync with WorldMixin)
    private static final int MIN_BUILD_HEIGHT = -64;
    private static final int MAX_BUILD_HEIGHT = 319;
    private static final int WORLD_HEIGHT = MAX_BUILD_HEIGHT - MIN_BUILD_HEIGHT + 1; // 384

    /**
     * @author YourName
     * @reason Make the sky and world border use the new height
     */
    @Overwrite
    public int getHeight() {
        return WORLD_HEIGHT;
    }

    /**
     * @author YourName
     * @reason Fix the actual height used for horizon and fog
     */
    @Overwrite
    public int getActualHeight() {
        return WORLD_HEIGHT;
    }

    // hasSkyLight() still returns true for overworld, which is fine.
    // The skylight calculation in Chunk will automatically work with negative sections
    // as long as we don't clamp the Y values. No need to change getLightBrightnessTable().
}