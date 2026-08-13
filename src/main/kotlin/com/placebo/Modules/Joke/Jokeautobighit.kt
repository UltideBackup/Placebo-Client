package com.placebo.Modules.Joke

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft

class Jokeautobighit: Module("autobighit") {
    override var state = false
    val mc = Minecraft.getInstance()
    override var category = "Joke"

    override fun Tick() {
        if (state == true) {
            if (mc.player?.swinging == true) {
                mc.options.keyShift.isDown = true//beautifill architecture for my big hits that bypiss hypix
                mc.options.keyLeft.isDown = true
            }
        }
    }
}