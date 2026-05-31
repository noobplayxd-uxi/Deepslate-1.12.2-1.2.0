package com.yourname.deepslateexpansion.mixins.minecraft.chunk;

import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.injection.Constant;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public abstract class ChunkMixin implements IChunkExtended {

    @Shadow
    private ExtendedBlockStorage[] storageArrays;

    @Shadow
    protected abstract boolean getWorldHasSkyLight();

    @Shadow
    public abstract World getWorld();

    private static final int MIN_SECTION_Y = -4;   // section index -4 = Y -64
    private static final int MAX_SECTION_Y = 19;   // section index 19 = Y 319
    private static final int SECTION_COUNT = 24;   // -4..19 inclusive

    // Expand the storage array from 16 to 24 inside the constructor.
    @ModifyConstant(method = "<init>*", constant = @Constant(intValue = 16))
    private int modifyArraySize(int original) {
        return SECTION_COUNT;
    }

    // Redirect getBlockStorageArray to handle negative/positive section indices.
    @Inject(method = "getBlockStorageArray", at = @At("HEAD"), cancellable = true)
    private void onGetBlockStorageArray(int sectionY, CallbackInfoReturnable<ExtendedBlockStorage> cir) {
        if (sectionY < MIN_SECTION_Y || sectionY > MAX_SECTION_Y) {
            cir.setReturnValue(null);
            return;
        }
        int index = sectionY - MIN_SECTION_Y;
        if (storageArrays == null || storageArrays.length != SECTION_COUNT) {
            storageArrays = new ExtendedBlockStorage[SECTION_COUNT];
        }
        cir.setReturnValue(storageArrays[index]);
    }

    // Safe implementation of loadExtendedSections that uses vanilla setBlockState.
    // This is called by ExtendedChunkHandler when the proxy sends our custom packet.
    @Override
    public void loadExtendedSections(int[] sectionYs, byte[][] blockDataArray,
                                      byte[][] blockLightArray, byte[][] skyLightArray,
                                      boolean groundUp) {
        // Not yet used – the proxy isn't sending extended chunks yet.
        // For now, this just ensures the chunk knows it has been modified.
        Chunk thisChunk = (Chunk)(Object)this;
        thisChunk.setModified(true);
        if (groundUp) {
            thisChunk.generateSkylightMap();
        }
    }
}
