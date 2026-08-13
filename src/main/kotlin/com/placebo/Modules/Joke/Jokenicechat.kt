package com.placebo.Modules.Joke

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft

class Jokenicechat: Module("nicechat") {
    override var state = false
    val mc = Minecraft.getInstance()
var chat = true
    override var category = "Joke"

    override fun Tick() {
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