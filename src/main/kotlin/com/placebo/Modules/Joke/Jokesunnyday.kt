package com.placebo.Modules.Joke

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft


class Jokesunnyday: Module("sunnyday") {
override var state = false
    val mc = Minecraft.getInstance()
    override var category = "Joke"


    override fun Tick() {

        if(state==true){
           mc.options.gamma().set(0.0)
            println("its so bright that my eyes are fucking burning")
        }
    }
}