package com.yourname.deepslateexpansion;   // <-- 1. The package must match your folder structure

// 2. These imports bring in Forge's required classes
import net.minecraftforge.fml.common.Mod;                  // Marks this class as a mod
import net.minecraftforge.fml.common.Mod.EventHandler;     // Tells Forge which methods to call
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;  // Early setup event
import net.minecraftforge.fml.common.event.FMLInitializationEvent;     // Main setup event
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent; // Late setup event

import com.yourname.deepslateexpansion.blocks.ModBlocks; // 3. Import our block registry

@Mod(modid = DeepslateExpansion.MODID, name = DeepslateExpansion.NAME, version = DeepslateExpansion.VERSION)
public class DeepslateExpansion {

    // 4. Constants that identify your mod. Forge uses MODID everywhere.
    public static final String MODID = "deepslateexpansion";
    public static final String NAME = "Deepslate Expansion";
    public static final String VERSION = "1.0.0";

    // 5. Pre-Init: This runs FIRST. Use it to create blocks/items, read configs, etc.
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // This call creates all our block instances (they aren't registered yet, just created)
        ModBlocks.init();
    }

    // 6. Init: Runs SECOND. Use it for recipes, event handlers, etc.
    @EventHandler
    public void init(FMLInitializationEvent event) {
        // We'll add recipes later, for now just a log message to confirm the mod loaded
        System.out.println(NAME + " has loaded successfully!");
    }

    // 7. Post-Init: Runs LAST. Use it for cross-mod interaction, once all mods are ready.
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // Nothing needed yet
    }
}