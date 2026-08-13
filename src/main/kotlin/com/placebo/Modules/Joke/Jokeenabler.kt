package com.placebo.Modules.Joke

import com.placebo.Core.modules
import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft


class Jokeenabler: Module("enabler") {//enables perfect enhanced figure of your imaginationless being
override var state = false
    val mc = Minecraft.getInstance()
    override var category = "Joke"


    override fun Tick() {

        if(state==true){
         for (module in modules){
             if (module !is Jokeenabler){//insane enabletech
                 module.state = false
             }
         }
        }
    }
}