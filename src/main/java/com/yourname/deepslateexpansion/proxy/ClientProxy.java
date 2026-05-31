package com.yourname.deepslateexpansion.proxy;

import com.yourname.deepslateexpansion.network.ExtendedChunkHandler;
import com.yourname.deepslateexpansion.events.ChunkReplaceHandler;
import com.yourname.deepslateexpansion.events.DeepslateReplacer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("extchunk");

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        // Register the extended chunk packet handler
        NETWORK.registerMessage(ExtendedChunkHandler.class, ExtendedChunkHandler.class, 0, Side.CLIENT);
        // Register the chunk load replacement handler (optional, harmless)
        MinecraftForge.EVENT_BUS.register(new ChunkReplaceHandler());
        // Register the tick‑based replacement scanner – guaranteed to work
        MinecraftForge.EVENT_BUS.register(new DeepslateReplacer());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
