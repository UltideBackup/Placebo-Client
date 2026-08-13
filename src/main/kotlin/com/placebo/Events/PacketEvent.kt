package com.placebo.Events

import com.placebo.Core.Event
import net.minecraft.network.protocol.Packet

enum class Direction {INCOMING, OUTGOING}
class PacketEvent(val direction: Direction, val packet: Packet<*>): Event {
    var isCancelled = false
}