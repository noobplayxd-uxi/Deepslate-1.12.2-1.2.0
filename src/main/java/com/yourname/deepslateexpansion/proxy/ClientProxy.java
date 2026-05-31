package com.yourname.deepslateexpansion.proxy;

import com.yourname.deepslateexpansion.network.ExtendedChunkHandler;
import com.yourname.deepslateexpansion.events.ChunkReplaceHandler;
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
        NETWORK.registerMessage(ExtendedChunkHandler.class, ExtendedChunkHandler.class, 0, Side.CLIENT);
        MinecraftForge.EVENT_BUS.register(new ChunkReplaceHandler());
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
