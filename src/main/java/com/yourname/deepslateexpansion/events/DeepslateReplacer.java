package com.yourname.deepslateexpansion.events;

import com.yourname.deepslateexpansion.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class DeepslateReplacer {

    private int tickCounter = 0;
    // Cache of chunk coordinates that have already been corrected
    private final Set<Long> correctedChunks = new HashSet<>();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        tickCounter++;
        if (tickCounter % 20 != 0) return;   // once per second

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) return;

        // Only Overworld – preserves Nether fortresses
        if (mc.player.dimension != 0) return;

        int radius = 4;
        int playerChunkX = mc.player.chunkCoordX;
        int playerChunkZ = mc.player.chunkCoordZ;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int cx = playerChunkX + dx;
                int cz = playerChunkZ + dz;
                long key = (long)cx << 32 | (cz & 0xFFFFFFFFL);

                // Skip if already corrected
                if (correctedChunks.contains(key)) continue;

                Chunk chunk = mc.world.getChunkProvider().provideChunk(cx, cz);
                if (chunk != null && !chunk.isEmpty()) {
                    // Replace nether bricks with deepslate (Overworld only)
                    replaceWrongBlock(chunk, Blocks.NETHER_BRICK, ModBlocks.deepslate);

                    // Mark as corrected
                    correctedChunks.add(key);
                }
            }
        }
    }

    private void replaceWrongBlock(Chunk chunk, Block wrongBlock, Block correctBlock) {
        int minY = -64;
        int maxY = 319;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minY; y <= maxY; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (chunk.getBlockState(pos).getBlock() == wrongBlock) {
                        chunk.setBlockState(pos, correctBlock.getDefaultState());
                    }
                }
            }
        }
    }
}
