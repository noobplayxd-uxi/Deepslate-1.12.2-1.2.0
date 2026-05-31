package com.yourname.deepslateexpansion.mixins.minecraft.world;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(World.class)
public abstract class WorldMixin {

    // Change the minimum Y constant from 0 to -64 inside isOutsideBuildHeight
    @ModifyConstant(method = "isOutsideBuildHeight", constant = @Constant(intValue = 0, ordinal = 0))
    private int modifyMinHeight(int original) {
        return -64;
    }

    // Change the maximum Y constant from 256 to 320 inside isOutsideBuildHeight
    @ModifyConstant(method = "isOutsideBuildHeight", constant = @Constant(intValue = 256))
    private int modifyMaxHeight(int original) {
        return 320;
    }
}
