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

    // Use the obfuscated name + remap = false to guarantee injection
    @Inject(method = "func_184376_a", at = @At("TAIL"), remap = false)
    private void onHandleChunkData(SPacketChunkData packet, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) return;

        int chunkX = packet.getChunkX();
        int chunkZ = packet.getChunkZ();
        Chunk chunk = mc.world.getChunkProvider().provideChunk(chunkX, chunkZ);
        if (chunk == null) return;

        // This will now appear in the console
        System.out.println("[DeepslateExpansion] Override mixin fired for chunk " + chunkX + ", " + chunkZ);

        // Replace nether bricks (ID 112) with deepslate
        replaceWrongBlock(chunk, Blocks.NETHER_BRICK, ModBlocks.deepslate);

        // TODO: Add ore replacements later
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
