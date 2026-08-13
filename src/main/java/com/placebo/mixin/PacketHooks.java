package com.placebo.mixin;


import com.placebo.Core.EventManagerKt;
import com.placebo.Events.Direction;
import com.placebo.Events.PacketEvent;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.Connection;



@Mixin(Connection.class)
public class PacketHooks {
    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/protocol/Packet;)V",cancellable = true)
    private void outgoingPackets(Packet<?> packet,CallbackInfo ci) {
        PacketEvent event = new PacketEvent(Direction.OUTGOING,packet);
        EventManagerKt.callEvent(event);
        if (event.isCancelled() == true){
            ci.cancel();
        }


    }
    @Inject(at = @At("HEAD"), method = "genericsFtw",cancellable = true)
    private static void incomingPackets(Packet<?> packet, PacketListener listener,CallbackInfo ci) {
        PacketEvent event = new PacketEvent(Direction.INCOMING,packet);
        EventManagerKt.callEvent(event);
        if (event.isCancelled() == true){
            ci.cancel();
        }
    }
}

