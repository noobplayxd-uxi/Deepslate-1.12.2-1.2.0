package com.yourname.deepslateexpansion.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    // Called during pre-initialization (before blocks are registered)
    public void preInit(FMLPreInitializationEvent event) {
    }

    // Called during initialization (after blocks are registered)
    public void init(FMLInitializationEvent event) {
    }

    // Called during post-initialization (all mods are ready)
    public void postInit(FMLPostInitializationEvent event) {
    }
}