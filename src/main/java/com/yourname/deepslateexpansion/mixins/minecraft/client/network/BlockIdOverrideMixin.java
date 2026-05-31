package com.yourname.deepslateexpansion.mixins.minecraft.client.network;

import com.yourname.deepslateexpansion.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.LocalCapture;

@Mixin(NetHandlerPlayClient.class)
public abstract class BlockIdOverrideMixin {

    // This injector runs AFTER the vanilla chunk data handler, so the chunk is already filled.
    // We then scan the chunk for any blocks that the proxy sent as wrong IDs and replace them
    // with the correct deepslate blocks.
    @Inject(method = "handleChunkData",
            at = @At("TAIL"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onChunkData(SPacketChunkData packet, CallbackInfo ci, Chunk chunk) {
        if (chunk == null) return;

        // Replace nether bricks with deepslate
        replaceWrongBlock(chunk, Blocks.NETHER_BRICK, ModBlocks.deepslate);

        // Add more ore overrides here once you identify which wrong ID the proxy sends for each ore.
        // For example, if the proxy sends red nether bricks for iron ore, you would add:
        // replaceWrongBlock(chunk, Blocks.RED_NETHER_BRICK, ModBlocks.deepslateIronOre);
        //
        // To find out what wrong IDs are being sent, join the server, find the blocks,
        // and note what they look like. Then add the appropriate replacement.
    }

    private void replaceWrongBlock(Chunk chunk, Block wrongBlock, Block correctBlock) {
        // Scan every position in the chunk (the chunk may only have sections for Y≥0,
        // but we scan the full extended range in case the world‑height mixins are active)
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
