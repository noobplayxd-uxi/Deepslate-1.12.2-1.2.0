package com.yourname.deepslateexpansion.mixins.minecraft.world;

import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(WorldProvider.class)
public abstract class WorldProviderMixin {

    @ModifyConstant(method = "getHeight", constant = @Constant(intValue = 256))
    private int modifyGetHeight(int original) {
        return 384;
    }

    @ModifyConstant(method = "getActualHeight", constant = @Constant(intValue = 256))
    private int modifyGetActualHeight(int original) {
        return 384;
    }
}
