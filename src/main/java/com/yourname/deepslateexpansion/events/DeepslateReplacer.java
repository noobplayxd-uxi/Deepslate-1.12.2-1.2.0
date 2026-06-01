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

        // Only replace in the Overworld (dimension 0)
        // This preserves Nether fortresses and other nether brick structures.
        if (mc.player.dimension != 0) return;

        int radius = 4;
        int playerChunkX = mc.player.chunkCoordX;
        int playerChunkZ = mc.player.chunkCoordZ;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                Chunk chunk = mc.world.getChunkProvider().provideChunk(playerChunkX + dx, playerChunkZ + dz);
                if (chunk != null && !chunk.isEmpty()) {
                    // Replace nether bricks with deepslate (all Y levels, Overworld only)
                    replaceWrongBlock(chunk, Blocks.NETHER_BRICK, ModBlocks.deepslate);

                    // Replace vanilla ores with deepslate ores (add as you identify them)
                    // These will only affect ores that the proxy maps to vanilla variants.
                    replaceWrongBlock(chunk, Blocks.IRON_ORE, ModBlocks.deepslateIronOre);
                    replaceWrongBlock(chunk, Blocks.GOLD_ORE, ModBlocks.deepslateGoldOre);
                    replaceWrongBlock(chunk, Blocks.COAL_ORE, ModBlocks.deepslateCoalOre);
                    replaceWrongBlock(chunk, Blocks.DIAMOND_ORE, ModBlocks.deepslateDiamondOre);
                    replaceWrongBlock(chunk, Blocks.EMERALD_ORE, ModBlocks.deepslateEmeraldOre);
                    replaceWrongBlock(chunk, Blocks.REDSTONE_ORE, ModBlocks.deepslateRedstoneOre);
                    replaceWrongBlock(chunk, Blocks.LAPIS_ORE, ModBlocks.deepslateLapisOre);
                    // Copper ore may need a separate fallback (e.g., Blocks.STONE) – find it with F3+H.
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
