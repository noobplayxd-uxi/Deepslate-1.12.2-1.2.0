package com.yourname.deepslateexpansion.proxy;

import com.yourname.deepslateexpansion.events.DeepslateReplacer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        // Register the tick‑based deepslate replacer (Overworld only)
        MinecraftForge.EVENT_BUS.register(new DeepslateReplacer());
    }
}
