package com.yourname.deepslateexpansion.mixins.minecraft.chunk;

public interface IChunkExtended 
{
    /**
     * Loads raw section data into the chunk, including negative Y sections.
     * Called from ExtendedChunkHandler when a custom extended chunk packet arrives.
     *
     * @param sectionYs       array of section Y indices (e.g., -4, -3, ... 19)
     * @param blockDataArray  raw block data for each section (may be null)
     * @param blockLightArray block light nibble arrays
     * @param skyLightArray   sky light nibble arrays
     * @param groundUp        if true, recalculate heightmaps and skylight
     */
    void loadExtendedSections(int[] sectionYs, byte[][] blockDataArray,
                              byte[][] blockLightArray, byte[][] skyLightArray,
                              boolean groundUp);
}
