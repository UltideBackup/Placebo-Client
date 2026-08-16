package com.placebo.Modules.Movement

import com.placebo.Core.Classes.Module
import com.placebo.Core.Event
import com.placebo.Core.Listener
import com.placebo.Events.FallEvent
import com.placebo.Events.PacketEvent
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket

class Nofall: Module("Nofall"), Listener {
    override var mode = 0
    override var state = false
    override var category = "Movement"
    override fun onEvent(event: Event) {
        if (state) {
            when (mode) {
                0 -> GroundSpoof(event)
            }
        }
    }
fun GroundSpoof(event: Event){
    if (event is PacketEvent) {
        if (event.packet is ServerboundMovePlayerPacket){
            event.packet.onGround = true
        }
    }
}

}