package com.yourname.deepslateexpansion.mixins.minecraft.client.network;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketChunkData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public abstract class NetHandlerPlayClientMixin {

    /**
     * This method intercepts vanilla chunk packets.
     * Currently it does nothing — the extended chunk data arrives via a separate
     * Forge custom channel (see ExtendedChunkHandler). However, you can later
     * modify this method to cancel vanilla chunk processing if an extended chunk
     * has already been loaded, preventing flickering or overwrites.
     *
     * @param packet the vanilla chunk data packet
     * @param ci     mixin callback (can be cancelled)
     */
    @Inject(method = "handleChunkData", at = @At("HEAD"), cancellable = true)
    private void onHandleChunkData(SPacketChunkData packet, CallbackInfo ci) {
        // Placeholder – does not cancel the vanilla processing.
        // To ignore vanilla chunks when extended data is present, add logic here
        // and call ci.cancel().
    }
}