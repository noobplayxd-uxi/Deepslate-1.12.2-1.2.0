package com.yourname.deepslateexpansion.mixins.minecraft.world;

import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(WorldProvider.class)
public abstract class WorldProviderMixin {

    // Changes the constant 256 returned by getHeight() to 384
    @ModifyConstant(method = "getHeight", constant = @Constant(intValue = 256))
    private int modifyGetHeight(int original) {
        return 384;
    }

    // getActualHeight() also uses 256 internally; we override it the same way
    @ModifyConstant(method = "getActualHeight", constant = @Constant(intValue = 256))
    private int modifyGetActualHeight(int original) {
        return 384;
    }
}
