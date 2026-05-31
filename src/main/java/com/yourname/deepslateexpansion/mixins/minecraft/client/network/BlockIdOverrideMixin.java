package com.yourname.deepslateexpansion.mixins.minecraft.client.network;

import com.yourname.deepslateexpansion.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public abstract class BlockIdOverrideMixin {

    // The actual method name in 1.12.2 Forge is "processChunkData", not "handleChunkData"
    @Inject(method = "processChunkData", at = @At("TAIL"))
    private void onProcessChunkData(SPacketChunkData packet, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) return;

        int chunkX = packet.getChunkX();
        int chunkZ = packet.getChunkZ();
        Chunk chunk = mc.world.getChunkProvider().provideChunk(chunkX, chunkZ);
        if (chunk == null) return;

        // This will print in the game console (launcher log) every time a chunk loads
        System.out.println("[DeepslateExpansion] Override mixin fired for chunk " + chunkX + ", " + chunkZ);

        // Replace nether bricks with deepslate
        replaceWrongBlock(chunk, Blocks.NETHER_BRICK, ModBlocks.deepslate);

        // TODO: Add ore replacements when you identify their wrong block IDs
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
