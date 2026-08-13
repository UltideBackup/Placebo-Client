package com.placebo.Modules.Test
import com.placebo.Core.Classes.Module
import com.placebo.Core.Event
import com.placebo.Core.Listener
import com.placebo.Events.Direction
import com.placebo.Events.PacketEvent
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket

class PacketTest: Module("PacketTest"), Listener {
    override var state = false
    override var category = "Test"
var mc = Minecraft.getInstance()
    override fun onEvent(event: Event) {
        if(event is PacketEvent){
            if (event.packet is ClientboundSetEntityMotionPacket && event.direction == Direction.INCOMING && event.packet.id == mc.player?.id)
                event.isCancelled = true

        }
    }


}