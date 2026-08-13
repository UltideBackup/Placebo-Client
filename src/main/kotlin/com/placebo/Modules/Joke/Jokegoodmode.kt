package com.placebo.Modules.Joke

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft

class Jokegoodmode: Module("goodmode") {
override var state = false
    val mc = Minecraft.getInstance()
    override var category = "Joke"//idek what to put this as


    override fun Tick() {
        if (state == true) {
            if (mc.player?.isDeadOrDying == true){
                mc.stop()//intense bypass to forever achieve the acceleration of godmode
            }
        }
    }
}