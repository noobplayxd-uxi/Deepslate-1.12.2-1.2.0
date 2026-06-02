package com.yourname.deepslateexpansion.proxy;

import com.yourname.deepslateexpansion.events.ChunkReplaceHandler;
import com.yourname.deepslateexpansion.events.DeepslateReplacer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        // Register the chunk‑load replacement handler (optional, harmless)
        MinecraftForge.EVENT_BUS.register(new ChunkReplaceHandler());
        // Register the tick‑based replacement scanner (deepslate above Y=0)
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
