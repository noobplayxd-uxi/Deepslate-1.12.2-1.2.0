package com.yourname.deepslateexpansion.proxyplugin;

import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.protocol.packet.*;

public class DeepslateProxyPlugin extends Plugin implements Listener {

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, this);
    }

    @EventHandler
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacket() instanceof MapChunkPacket) {
            MapChunkPacket packet = (MapChunkPacket) event.getPacket();
            // Expand bitmask to cover 24 sections (Y -4 to 19)
            int newBitmask = (1 << 24) - 1;
            packet.setBitmask(newBitmask);
        }
    }
}
