package com.placebo.Modules.Movement
import com.placebo.Core.Classes.Module
import com.placebo.Core.Event
import com.placebo.Core.Listener
import com.placebo.Events.MoveEvent
import net.minecraft.client.Minecraft


class Speed:Module("Speed"), Listener {
    override var state = false
    override var mode = 0
    override var category = "Movement"
    var mc = Minecraft.getInstance()

    override fun onEvent(event: Event) {
        if (state == true) {
            when (mode) {
                0 -> Vanilla(event)
            }
        }
    }

    private fun Vanilla(event: Event) {
        if(event is MoveEvent) {
        val player = mc.player ?: return

        val forward = if (player.input.keyPresses.forward) 1.0 else if (player.input.keyPresses.backward) -1.0 else 0.0
        val strafe = if (player.input.keyPresses.left) 1.0 else if (player.input.keyPresses.right) -1.0 else 0.0
        val speed = 2.0

        var yaw = Math.toRadians(player.getYRot().toDouble())
        event.deltaMove.x = (-Math.sin(yaw) * forward + Math.cos(yaw) * strafe) * speed
        event.deltaMove.z = (Math.cos(yaw) * forward + Math.sin(yaw) * strafe) * speed
    }
    }
    }