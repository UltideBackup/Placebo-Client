package com.placebo.codethatidontknowwhatitdoes

import com.placebo.Useless.uselessclasses.coide
import net.minecraft.client.Minecraft
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.world.phys.Vec3
import kotlin.random.Random

class nicechat: coide("nicechat") {
    override var state = false
    val mc = Minecraft.getInstance()
var chat = true

    override fun Tiick() {
        if (state == true) {
            if (mc.player?.isDeadOrDying == false){
                chat = true
            }
            if (mc.player?.isDeadOrDying == true){
                if (chat == true){
                    mc.connection?.sendChat("YOURE FUCKING EZ KID I WAS GOING EASY")//extremely nice phrase
                    mc.options.fov().set(30)
                    println("the message was so nice so now youre vision is zoomed in to get a better look at your life")
                    chat = false
                    return
                }

            }
        }
    }
}