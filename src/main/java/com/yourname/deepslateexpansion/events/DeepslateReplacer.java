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

@SideOnly(Side.CLIENT)
public class DeepslateReplacer {

    private int tickCounter = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        tickCounter++;
        if (tickCounter % 20 != 0) return;   // run once per second

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) return;

        // Debug message – watch the console to confirm the replacer is running
        System.out.println("[DeepslateExpansion] Tick scanner running. Ticks: " + tickCounter);

        int radius = 4;   // chunks around the player
        int playerChunkX = mc.player.chunkCoordX;
        int playerChunkZ = mc.player.chunkCoordZ;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                Chunk chunk = mc.world.getChunkProvider().provideChunk(playerChunkX + dx, playerChunkZ + dz);
                if (chunk != null && !chunk.isEmpty()) {
                    replaceWrongBlock(chunk, Blocks.NETHER_BRICK, ModBlocks.deepslate);
                    // TODO: Add ore replacements later
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
