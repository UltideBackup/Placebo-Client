package com.placebo.Modules.Movement
import com.placebo.Core.Classes.Module
import com.placebo.Core.Event
import com.placebo.Core.Listener
import com.placebo.Events.Direction
import com.placebo.Events.PacketEvent
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket

class Blink:Module("Blink"), Listener {

    override var state = false
    override var category = "Movement"
    var mc = Minecraft.getInstance()
    private val packets = mutableListOf<Packet<*>>()

    override fun onEvent(event: Event) {
        if(event is PacketEvent && state) {
            if (event.packet is ServerboundMovePlayerPacket&& event.direction == Direction.OUTGOING){
                packets.add(event.packet)
                event.isCancelled = true
            }
        }

    }

    override fun Tick() {
        if (!state){
            for (packet in packets){
                mc.connection?.send(packet)
            }
            packets.clear()
        }
    }
}