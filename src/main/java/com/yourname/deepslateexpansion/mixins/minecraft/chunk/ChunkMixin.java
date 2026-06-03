package com.yourname.deepslateexpansion.mixins.minecraft.chunk;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public abstract class ChunkMixin implements IChunkExtended {

    @Shadow private ExtendedBlockStorage[] storageArrays;
    @Shadow public abstract World getWorld();

    private static final int MIN_SECTION_Y = -4;
    private static final int MAX_SECTION_Y = 19;
    private static final int SECTION_COUNT = 24;

    // Expand the storage array from 16 to 24 in the constructor
    @ModifyConstant(method = "<init>*", constant = @Constant(intValue = 16))
    private int modifyArraySize(int original) {
        return SECTION_COUNT;
    }

    // Redirect getBlockStorageArray to handle negative/positive section indices
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

    // Fully working extended section loader
    @Override
    public void loadExtendedSections(int[] sectionYs, byte[][] blockDataArray,
                                      byte[][] blockLightArray, byte[][] skyLightArray,
                                      boolean groundUp) {
        // Ensure the storage array is the correct size
        if (storageArrays == null || storageArrays.length != SECTION_COUNT) {
            storageArrays = new ExtendedBlockStorage[SECTION_COUNT];
        }

        // We need to know if this world has sky light
        World world = getWorld();
        boolean hasSkyLight = world != null && world.provider.hasSkyLight();

        for (int i = 0; i < sectionYs.length; i++) {
            int sectionY = sectionYs[i];
            int idx = sectionY - MIN_SECTION_Y;

            ExtendedBlockStorage storage = storageArrays[idx];
            if (storage == null) {
                // Create a new section with the correct Y and sky light flag
                storage = new ExtendedBlockStorage(sectionY * 16, hasSkyLight);
                storageArrays[idx] = storage;
            }

            // Set block data
            if (blockDataArray[i] != null) {
                try {
                    // Use the public setBlockData method if it exists
                    java.lang.reflect.Method method = ExtendedBlockStorage.class
                            .getMethod("setBlockData", byte[].class);
                    method.invoke(storage, (Object) blockDataArray[i]);
                } catch (Exception e) {
                    // Fallback: set blocks one by one
                    setBlockDataFallback(storage, blockDataArray[i]);
                }
            }

            // Set light arrays via reflection (no direct setters)
            if (blockLightArray[i] != null) {
                setLightArray(storage, "blockLight", blockLightArray[i]);
            }
            if (skyLightArray[i] != null) {
                setLightArray(storage, "skyLight", skyLightArray[i]);
            }
        }

        if (groundUp) {
            ((Chunk)(Object)this).generateSkylightMap();
        }
        ((Chunk)(Object)this).setModified(true);
    }

    private void setBlockDataFallback(ExtendedBlockStorage storage, byte[] data) {
        char[] chars = new char[4096];
        for (int i = 0; i < 4096; i++) {
            chars[i] = (char) ((data[i*2] & 0xFF) << 8 | (data[i*2+1] & 0xFF));
        }
        for (int y = 0; y < 16; y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    int idx = (y << 8) | (z << 4) | x;
                    char packed = chars[idx];
                    int blockId = (packed >> 4) & 0xFFF;
                    int meta = packed & 0xF;
                    storage.set(x, y, z, Block.getBlockById(blockId).getStateFromMeta(meta));
                }
            }
        }
    }

    private void setLightArray(ExtendedBlockStorage storage, String fieldName, byte[] data) {
        try {
            java.lang.reflect.Field field = ExtendedBlockStorage.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            // The light arrays are NibbleArray instances; create a new one from the raw bytes
            Object nibbleArray = field.getType().getConstructor(byte[].class).newInstance((Object) data);
            field.set(storage, nibbleArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
