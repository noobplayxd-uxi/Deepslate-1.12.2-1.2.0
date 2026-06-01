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
        if (tickCounter % 20 != 0) return;   // once per second

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null) return;

        // Only replace in the Overworld (preserves Nether fortresses)
        if (mc.player.dimension != 0) return;

        int radius = 4;
        int playerChunkX = mc.player.chunkCoordX;
        int playerChunkZ = mc.player.chunkCoordZ;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                Chunk chunk = mc.world.getChunkProvider().provideChunk(playerChunkX + dx, playerChunkZ + dz);
                if (chunk != null && !chunk.isEmpty()) {
                    // Replace nether bricks with deepslate (only below Y=0)
                    replaceWrongBlockBelowY(chunk, 0, Blocks.NETHER_BRICK, ModBlocks.deepslate);

                    // Replace vanilla ores with deepslate ores (only below Y=0)
                    replaceWrongBlockBelowY(chunk, 0, Blocks.IRON_ORE, ModBlocks.deepslateIronOre);
                    replaceWrongBlockBelowY(chunk, 0, Blocks.GOLD_ORE, ModBlocks.deepslateGoldOre);
                    replaceWrongBlockBelowY(chunk, 0, Blocks.COAL_ORE, ModBlocks.deepslateCoalOre);
                    replaceWrongBlockBelowY(chunk, 0, Blocks.DIAMOND_ORE, ModBlocks.deepslateDiamondOre);
                    replaceWrongBlockBelowY(chunk, 0, Blocks.EMERALD_ORE, ModBlocks.deepslateEmeraldOre);
                    replaceWrongBlockBelowY(chunk, 0, Blocks.REDSTONE_ORE, ModBlocks.deepslateRedstoneOre);
                    replaceWrongBlockBelowY(chunk, 0, Blocks.LAPIS_ORE, ModBlocks.deepslateLapisOre);

                    // Copper ore might not map to a vanilla ore; you'll have to find its fallback manually.
                    // Once you find what block it appears as (e.g., stone), add a line like:
                    // replaceWrongBlockBelowY(chunk, 0, Blocks.STONE, ModBlocks.deepslateCopperOre);
                }
            }
        }
    }

    /**
     * Replaces all occurrences of {@code wrongBlock} with {@code correctBlock}
     * only at Y levels strictly less than {@code maxY}.
     */
    private void replaceWrongBlockBelowY(Chunk chunk, int maxY, Block wrongBlock, Block correctBlock) {
        int minY = -64;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minY; y < maxY; y++) {   // only Y < maxY
                    BlockPos pos = new BlockPos(x, y, z);
                    if (chunk.getBlockState(pos).getBlock() == wrongBlock) {
                        chunk.setBlockState(pos, correctBlock.getDefaultState());
                    }
                }
            }
        }
    }
}
