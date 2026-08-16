package com.placebo.Modules.Combat

import com.placebo.Core.Classes.Module
import com.placebo.Core.Event
import com.placebo.Core.Listener
import com.placebo.Events.PacketEvent
import com.placebo.Utils.DelayHelper
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import kotlin.random.Random

class FakeLag: Module("FakeLag"), Listener {
    override var mode = 0
    override var state = false
    override var category = "Combat"
    var mc = Minecraft.getInstance()
    private val resetDelay = DelayHelper()
    private var isHolding = false
    private val packet = mutableListOf<Packet<*>>()
    var chance=0
    override fun onEvent(event: Event) {
        if (isHolding && event is PacketEvent && state) {
            if (event.packet is ServerboundMovePlayerPacket)
                packet.add(event.packet)
            event.isCancelled = true
        }
    }

    override fun Tick() {
        chance = Random.nextInt(0,100)
        if (chance >= 75&& chance < 90) {
            isHolding = true
        } else if (chance >= 90) {
            isHolding = false
        }
        if(state && !isHolding) {
release()
        }
    }
    private fun release() {
        if (state && !isHolding) {
            for (packet in packet){
                mc.connection?.send(packet)
            }
            packet.clear()
        }
    }
}

