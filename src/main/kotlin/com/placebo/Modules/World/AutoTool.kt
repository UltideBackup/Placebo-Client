package com.placebo.Modules.World
import com.placebo.Core.Classes.Module
import com.placebo.Core.Event
import com.placebo.Core.Listener
import com.placebo.Events.PacketEvent
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket


class AutoTool :Module("Auto Tool"),Listener {
    override var state = false
    override var mode = 0
    override var category = "World"
    var mc = Minecraft.getInstance()


    override fun onEvent(event: Event) {
        var level = mc.level?:return
var player = mc.player?:return
        if (event is PacketEvent && event.packet is ServerboundPlayerActionPacket && state) {
            if (event.packet.action == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
                val pos = event.packet.pos
                val blockState = level.getBlockState(pos)
                var bestSlot = -1
                var bestSpeed = 1.0f

                for (i in 0..8){
                    val stack = player.inventory.getItem(i)
                    val speed = stack.getDestroySpeed(blockState)
                    if (speed > bestSpeed){
                        bestSlot = i
                        bestSpeed = speed
                    }
                }
                if (bestSlot != -1){
                    player.inventory.selectedSlot = bestSlot
                }
            }
        }
    }
}