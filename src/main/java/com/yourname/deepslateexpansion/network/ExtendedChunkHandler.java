package com.yourname.deepslateexpansion.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ExtendedChunkHandler implements IMessage, IMessageHandler<ExtendedChunkHandler, IMessage> {

    private int chunkX;
    private int chunkZ;
    private boolean groundUpContinuous;
    private int sectionCount;
    private int[] sectionY;
    private byte[][] blockData;
    private byte[][] blockLight;
    private byte[][] skyLight;

    public ExtendedChunkHandler() {}

    public ExtendedChunkHandler(int chunkX, int chunkZ, boolean groundUp, int count,
                                 int[] y, byte[][] blocks, byte[][] blockLight, byte[][] skyLight) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.groundUpContinuous = groundUp;
        this.sectionCount = count;
        this.sectionY = y;
        this.blockData = blocks;
        this.blockLight = blockLight;
        this.skyLight = skyLight;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        chunkX = packet.readInt();
        chunkZ = packet.readInt();
        groundUpContinuous = packet.readBoolean();
        sectionCount = packet.readVarInt();

        sectionY = new int[sectionCount];
        blockData = new byte[sectionCount][];
        blockLight = new byte[sectionCount][];
        skyLight = new byte[sectionCount][];

        for (int i = 0; i < sectionCount; i++) {
            sectionY[i] = packet.readInt();
            int blockDataLen = packet.readVarInt();
            blockData[i] = new byte[blockDataLen];
            packet.readBytes(blockData[i]);

            int lightLen = packet.readVarInt();
            blockLight[i] = new byte[lightLen];
            packet.readBytes(blockLight[i]);

            lightLen = packet.readVarInt();
            skyLight[i] = new byte[lightLen];
            packet.readBytes(skyLight[i]);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // Not used on the client
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(ExtendedChunkHandler message, MessageContext ctx) {
        // Will be fully implemented once the proxy plugin sends the extended chunk packet.
        // For now it does nothing, allowing compilation to succeed.
        return null;
    }
}
