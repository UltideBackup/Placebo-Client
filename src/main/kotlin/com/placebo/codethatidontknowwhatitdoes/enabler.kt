package com.placebo.codethatidontknowwhatitdoes

import com.placebo.Useless.modules
import com.placebo.Useless.uselessclasses.coide
import net.minecraft.client.Minecraft


class enabler: coide("enabler") {//enables perfect enhanced figure of your imaginationless being
override var state = false
    val mc = Minecraft.getInstance()


    override fun Tiick() {

        if(state==true){
         for (module in modules){
             if (module !is enabler){//insane enabletech
                 module.state = false
             }
         }
        }
    }
}