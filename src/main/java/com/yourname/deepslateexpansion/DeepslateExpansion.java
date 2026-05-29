package com.yourname.deepslateexpansion;

import com.yourname.deepslateexpansion.Blocks.ModBlocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = DeepslateExpansion.MODID, name = DeepslateExpansion.NAME, version = DeepslateExpansion.VERSION)
public class DeepslateExpansion {

    public static final String MODID = "deepslateexpansion";
    public static final String NAME = "Deepslate Expansion";
    public static final String VERSION = "1.0.0";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // This creates all our block instances.
        ModBlocks.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println(NAME + " has loaded successfully!");
    }
}
