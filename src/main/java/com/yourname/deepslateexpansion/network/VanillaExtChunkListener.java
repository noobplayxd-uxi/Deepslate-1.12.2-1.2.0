package com.yourname.deepslateexpansion.network;

import com.yourname.deepslateexpansion.mixins.minecraft.chunk.IChunkExtended;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VanillaExtChunkListener {

    private static boolean channelRegistered = false;

    public static void registerExtChunkChannel() {
        if (channelRegistered) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getConnection() != null) {
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            buffer.writeString("extchunk");
            CPacketCustomPayload registerPacket = new CPacketCustomPayload("REGISTER", buffer);
            mc.getConnection().sendPacket(registerPacket);
            channelRegistered = true;
            System.out.println("[DeepslateExpansion] Registered extchunk channel with proxy.");
        }
    }

    @SubscribeEvent
    public void onCustomPacket(ClientCustomPacketEvent event) {
        FMLProxyPacket proxyPacket = event.getPacket();
        if ("extchunk".equals(proxyPacket.channel())) {
            System.out.println("[DeepslateExpansion] Received extchunk packet. Payload size: " + proxyPacket.payload().readableBytes());
            handleExtendedChunk(new PacketBuffer(proxyPacket.payload().copy()));
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
                System.out.println("[DeepslateExpansion] Extended sections loaded for chunk " + chunkX + "," + chunkZ);

                // Force re‑render so the blocks become visible
                int blockX = chunkX << 4;
                int blockZ = chunkZ << 4;
                mc.world.markBlockRangeForRenderUpdate(blockX, -64, blockZ,
                                                       blockX + 15, 319, blockZ + 15);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
