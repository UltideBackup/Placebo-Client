package com.placebo.Modules.Combat

import com.placebo.Core.Classes.Module
import com.placebo.Core.Event
import com.placebo.Core.Listener
import com.placebo.Events.Direction
import com.placebo.Events.PacketEvent
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket

class Velocity: Module("Velocity"), Listener {
    override var state = false
    override var category = "Combat"
    override var mode = 1//make this configurable
    var mc = Minecraft.getInstance()
    override fun onEvent(event: Event) {
        if (state == true){
            when (mode) {
                0 -> CancelPacket(event)
                1 -> JumpReset(event)
                2 -> SpoofPacket(event)
            }
    }
}//yandere sim ass if statements
    //and im not fixing it

    private fun CancelPacket(event: Event) {
        if (event is PacketEvent && event.packet is ClientboundSetEntityMotionPacket && event.direction == Direction.INCOMING && event.packet.id == mc.player?.id) {
event.isCancelled = true
        }
    }

    private fun JumpReset(event: Event) {
        if (event is PacketEvent && event.packet is ClientboundSetEntityMotionPacket && event.direction == Direction.INCOMING && event.packet.id == mc.player?.id) {
        mc.player?.jumpFromGround()//this module does not remove velocity but reduces it
        }
    }
    private fun SpoofPacket(event: Event) {
        if (event is PacketEvent && event.packet is ClientboundSetEntityMotionPacket && event.direction == Direction.INCOMING && event.packet.id == mc.player?.id) {
            event.packet.movement.x = 0.0
            event.packet.movement.y = 0.0
            event.packet.movement.z = 0.0//this should work because of my accesswidener
        }
    }
}