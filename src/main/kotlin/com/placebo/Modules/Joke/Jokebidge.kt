package com.placebo.Modules.Joke

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft
import kotlin.random.Random

class Jokebidge: Module("bidge") {
    override var state = false
    val mc = Minecraft.getInstance()
    override var category = "Joke"

    override fun Tick() {
        if (state == true) {
            mc.options.keyShift.isDown = true//safe walk
            mc.options.keyUse.isDown = true//bidge method
            var bidgebypasspixels = Random.nextInt(1, 100)
            if (bidgebypasspixels == 62) {//extreme packet manipulation for bypassing
                mc.player?.inventory?.dropAll()//dsont mind this for some reason this makes everything a ghost item which is better
                mc.player?.jumpFromGround()//jump to look legt
                mc.options.keyShift.isDown = false//safe walk
                mc.options.keyLeft.isDown = false//bidge method
                mc.options.keyDown.isDown = true//pray and hope you fall off the edge type shit


            }

        }
    }
}