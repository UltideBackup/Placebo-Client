package com.placebo.Modules.Movement

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft

class Jetpack : Module("Jetpack") {

    override var state = false


    override fun Tick() {
        var client = Minecraft.getInstance()
        var player = client.player
        if (state && client.options.keyJump.isDown && player != null) {
            var x = player.deltaMovement.x()
            var z = player.deltaMovement.z()
            player.setDeltaMovement(x, 1.0, z)
        }
    }



}