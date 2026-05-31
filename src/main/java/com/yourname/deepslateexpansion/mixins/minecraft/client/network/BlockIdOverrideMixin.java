package com.yourname.deepslateexpansion.mixins.minecraft.client.network;

import com.yourname.deepslateexpansion.Blocks.ModBlocks;
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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(NetHandlerPlayClient.class)
public abstract class BlockIdOverrideMixin {

    @Inject(method = "handleChunkData",
            at = @At("TAIL"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onChunkData(SPacketChunkData packet, CallbackInfo ci, Chunk chunk) {
        if (chunk == null) return;

        replaceWrongBlock(chunk, Blocks.NETHER_BRICK, ModBlocks.deepslate);

        // Add more ore overrides here later when you identify the wrong IDs.
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
