package com.yourname.deepslateexpansion.network;

import com.yourname.deepslateexpansion.mixins.minecraft.chunk.IChunkExtended;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VanillaExtChunkListener {

    @SubscribeEvent
    public void onCustomPacket(ClientCustomPacketEvent event) {
        // FMLProxyPacket wraps the actual vanilla packet
        FMLProxyPacket proxyPacket = event.getPacket();
        // The channel name is stored in the packet
        if ("extchunk".equals(proxyPacket.channel())) {
            handleExtendedChunk(new PacketBuffer(proxyPacket.payload()));
            event.setCanceled(true);
        }
    }

    private void handleExtendedChunk(PacketBuffer buf) {
        try {
            int chunkX = buf.readInt();
            int chunkZ = buf.readInt();
            boolean groundUp = buf.readBoolean();
            int sectionCount = buf.readVarInt();

            int[] sectionY = new int[sectionCount];
            byte[][] blockData = new byte[sectionCount][];
            byte[][] blockLight = new byte[sectionCount][];
            byte[][] skyLight = new byte[sectionCount][];

            for (int i = 0; i < sectionCount; i++) {
                sectionY[i] = buf.readInt();
                int blockDataLen = buf.readVarInt();
                blockData[i] = new byte[blockDataLen];
                buf.readBytes(blockData[i]);

                int lightLen = buf.readVarInt();
                blockLight[i] = new byte[lightLen];
                buf.readBytes(blockLight[i]);

                lightLen = buf.readVarInt();
                skyLight[i] = new byte[lightLen];
                buf.readBytes(skyLight[i]);
            }

            Minecraft mc = Minecraft.getMinecraft();
            if (mc.world == null) return;

            ChunkProviderClient provider = mc.world.getChunkProvider();
            Chunk chunk = provider.provideChunk(chunkX, chunkZ);
            if (chunk == null) {
                chunk = new Chunk(mc.world, chunkX, chunkZ);
                provider.loadChunk(chunkX, chunkZ);
            }

            if (chunk instanceof IChunkExtended) {
                ((IChunkExtended) chunk).loadExtendedSections(
                    sectionY, blockData, blockLight, skyLight, groundUp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
