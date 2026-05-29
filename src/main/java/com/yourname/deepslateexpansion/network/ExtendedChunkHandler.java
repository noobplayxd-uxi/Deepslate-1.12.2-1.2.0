package com.yourname.deepslateexpansion.network;

import com.yourname.deepslateexpansion.mixins.minecraft.chunk.IChunkExtended;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Custom packet for receiving extended chunk data (up to Y=-64..319)
 * from the ViaBackwards proxy. Works in conjunction with the "extchunk"
 * network channel registered in ClientProxy.
 */
public class ExtendedChunkHandler implements IMessage, IMessageHandler<ExtendedChunkHandler, IMessage> {

    // Chunk coordinates and flags
    private int chunkX;
    private int chunkZ;
    private boolean groundUpContinuous;
    private int sectionCount;

    // Arrays that hold the raw section data
    private int[] sectionY;
    private byte[][] blockData;
    private byte[][] blockLight;
    private byte[][] skyLight;

    // Required empty constructor
    public ExtendedChunkHandler() {}

    /**
     * Constructor used by the proxy when sending the packet (not needed on the client).
     */
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

    /**
     * Reads the packet data from the network buffer.
     * Format:
     *   chunkX (int), chunkZ (int), groundUp (boolean), sectionCount (varint)
     *   For each section:
     *     sectionY (int), blockData length (varint), blockData bytes,
     *     blockLight length (varint), blockLight bytes,
     *     skyLight length (varint), skyLight bytes
     */
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
            sectionY[i] = packet.readInt();               // can be negative
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

    /**
     * Not used on the client (only the proxy sends data to us).
     */
    @Override
    public void toBytes(ByteBuf buf) {
        // No implementation needed
    }

    /**
     * Handles the packet on the client side: loads the extended chunk data
     * into the client's chunk cache and triggers a render update.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(ExtendedChunkHandler message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) return null;

        // 1. Get or create the chunk
        ChunkProviderClient provider = mc.world.getChunkProvider();
        Chunk chunk = provider.provideChunk(message.chunkX, message.chunkZ);
        if (chunk == null) {
            chunk = new Chunk(mc.world, message.chunkX, message.chunkZ);
            provider.loadChunk(message.chunkX, message.chunkZ, chunk);
        }

        // 2. Load the extended sections using the mixin interface
        if (chunk instanceof IChunkExtended) {
            ((IChunkExtended) chunk).loadExtendedSections(
                message.sectionY,
                message.blockData,
                message.blockLight,
                message.skyLight,
                message.groundUpContinuous
            );
        } else {
            System.err.println("Chunk does not implement IChunkExtended! Extended chunk loading failed.");
            return null;
        }

        // 3. Re-render the chunk (full height)
        mc.world.markBlockRangeForRenderUpdate(
            message.chunkX << 4, -64,
            message.chunkZ << 4,
            (message.chunkX << 4) + 15, 319,
            (message.chunkZ << 4) + 15
        );

        return null; // no reply needed
    }
}package com.yourname.deepslateexpansion.network;

import com.yourname.deepslateexpansion.mixins.minecraft.chunk.IChunkExtended;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Custom packet for receiving extended chunk data (up to Y=-64..319)
 * from the ViaBackwards proxy. Works in conjunction with the "extchunk"
 * network channel registered in ClientProxy.
 */
public class ExtendedChunkHandler implements IMessage, IMessageHandler<ExtendedChunkHandler, IMessage> {

    // Chunk coordinates and flags
    private int chunkX;
    private int chunkZ;
    private boolean groundUpContinuous;
    private int sectionCount;

    // Arrays that hold the raw section data
    private int[] sectionY;
    private byte[][] blockData;
    private byte[][] blockLight;
    private byte[][] skyLight;

    // Required empty constructor
    public ExtendedChunkHandler() {}

    /**
     * Constructor used by the proxy when sending the packet (not needed on the client).
     */
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

    /**
     * Reads the packet data from the network buffer.
     * Format:
     *   chunkX (int), chunkZ (int), groundUp (boolean), sectionCount (varint)
     *   For each section:
     *     sectionY (int), blockData length (varint), blockData bytes,
     *     blockLight length (varint), blockLight bytes,
     *     skyLight length (varint), skyLight bytes
     */
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
            sectionY[i] = packet.readInt();               // can be negative
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

    /**
     * Not used on the client (only the proxy sends data to us).
     */
    @Override
    public void toBytes(ByteBuf buf) {
        // No implementation needed
    }

    /**
     * Handles the packet on the client side: loads the extended chunk data
     * into the client's chunk cache and triggers a render update.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(ExtendedChunkHandler message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) return null;

        // 1. Get or create the chunk
        ChunkProviderClient provider = mc.world.getChunkProvider();
        Chunk chunk = provider.provideChunk(message.chunkX, message.chunkZ);
        if (chunk == null) {
            chunk = new Chunk(mc.world, message.chunkX, message.chunkZ);
            provider.loadChunk(message.chunkX, message.chunkZ, chunk);
        }

        // 2. Load the extended sections using the mixin interface
        if (chunk instanceof IChunkExtended) {
            ((IChunkExtended) chunk).loadExtendedSections(
                message.sectionY,
                message.blockData,
                message.blockLight,
                message.skyLight,
                message.groundUpContinuous
            );
        } else {
            System.err.println("Chunk does not implement IChunkExtended! Extended chunk loading failed.");
            return null;
        }

        // 3. Re-render the chunk (full height)
        mc.world.markBlockRangeForRenderUpdate(
            message.chunkX << 4, -64,
            message.chunkZ << 4,
            (message.chunkX << 4) + 15, 319,
            (message.chunkZ << 4) + 15
        );

        return null; // no reply needed
    }
}