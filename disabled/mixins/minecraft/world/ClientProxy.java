package com.yourname.deepslateexpansion.proxy;

import com.yourname.deepslateexpansion.network.ExtendedChunkHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

    // The network channel for receiving extended chunk data
    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("extchunk");

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        // Register our custom packet handler. This will receive the extended chunk data
        // from the ViaBackwards proxy and load it into the client's chunk storage.
        NETWORK.registerMessage(ExtendedChunkHandler.class, ExtendedChunkHandler.class, 0, Side.CLIENT);
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
