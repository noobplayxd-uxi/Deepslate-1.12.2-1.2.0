package com.yourname.deepslateexpansion;

import com.yourname.deepslateexpansion.blocks.ModBlocks;
import com.yourname.deepslateexpansion.network.VanillaExtChunkListener;
import com.yourname.deepslateexpansion.proxy.CommonProxy;
import com.yourname.deepslateexpansion.proxy.ProxyLauncher;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = DeepslateExpansion.MODID, name = DeepslateExpansion.NAME, version = DeepslateExpansion.VERSION)
public class DeepslateExpansion {
    public static final String MODID = "deepslateexpansion";
    public static final String NAME = "Deepslate Expansion";
    public static final String VERSION = "1.0.0";

    @SidedProxy(
        clientSide = "com.yourname.deepslateexpansion.proxy.ClientProxy",
        serverSide = "com.yourname.deepslateexpansion.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModBlocks.init();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Start the embedded proxy with Java 17 (via mini JRE)
        try {
            ProxyLauncher.startProxy();
        } catch (Exception e) {
            System.err.println("[DeepslateExpansion] Could not start proxy: " + e.getMessage());
        }

        // Register the vanilla extchunk listener
        MinecraftForge.EVENT_BUS.register(new VanillaExtChunkListener());

        // Automatically register the extchunk channel when the player joins a world
        MinecraftForge.EVENT_BUS.register(new Object() {
            @SubscribeEvent
            public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
                VanillaExtChunkListener.registerExtChunkChannel();
            }
        });

        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
