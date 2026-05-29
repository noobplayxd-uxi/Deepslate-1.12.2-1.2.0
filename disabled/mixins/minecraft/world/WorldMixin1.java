package com.yourname.deepslateexpansion.mixins.minecraft.world;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(World.class)
public abstract class WorldMixin {

    // Our extended world limits
    private static final int MIN_BUILD_HEIGHT = -64;   // bottom of the world
    private static final int MAX_BUILD_HEIGHT = 319;   // top block that can be placed
    private static final int WORLD_HEIGHT = MAX_BUILD_HEIGHT - MIN_BUILD_HEIGHT + 1; // 384

    /**
     * @author YourName
     * @reason Extend world height to -64..319
     */
    @Overwrite
    public int getHeight() {
        return WORLD_HEIGHT;   // 384
    }

    /**
     * @author YourName
     * @reason Extend actual build height
     */
    @Overwrite
    public int getActualHeight() {
        return WORLD_HEIGHT;
    }

    /**
     * @author YourName
     * @reason Allow blocks below Y=0 and above Y=255
     */
    @Overwrite
    public boolean isOutsideBuildHeight(BlockPos pos) {
        int y = pos.getY();
        return y < MIN_BUILD_HEIGHT || y > MAX_BUILD_HEIGHT;
    }

    /**
     * @author YourName
     * @reason Make isValid check use new limits
     */
    @Overwrite
    public boolean isValid(BlockPos pos) {
        return !isOutsideBuildHeight(pos);
    }

    // The sea level is still at Y=63, that's fine.
    // If you want to change it, overwrite getSeaLevel() here.
}
