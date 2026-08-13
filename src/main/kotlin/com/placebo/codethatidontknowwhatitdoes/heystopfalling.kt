package com.placebo.codethatidontknowwhatitdoes

import com.placebo.Useless.modules
import com.placebo.Useless.uselessclasses.coide
import net.minecraft.client.Minecraft
import net.minecraft.world.phys.Vec3


class heystopfalling: coide("heystopfalling") {
override var state = false
    val mc = Minecraft.getInstance()
    override var category = "Movement"

    override fun Tiick() {
var player = mc.player ?: return
        if(state==true){
            if (player.deltaMovement.y <0){
                var x = player.x
                var z = player.z

                mc.connection?.sendChat("FUCKING HELP ME IM FALLING")
                player.addDeltaMovement(Vec3(x, 10.0, z))
                mc.connection?.sendChat("im not falling anymore thank you")
            }
            }
        }
    }
