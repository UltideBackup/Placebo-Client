package com.placebo.codethatidontknowwhatitdoes

import com.placebo.Useless.uselessclasses.coide
import net.minecraft.client.Minecraft
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.world.phys.Vec3
import kotlin.random.Random

class goodmode: coide("goodmode") {
override var state = false
    val mc = Minecraft.getInstance()


    override fun Tiick() {
        if (state == true) {
            if (mc.player?.isDeadOrDying == true){
                mc.stop()//intense bypass to forever achieve the acceleration of godmode
            }
        }
    }
}