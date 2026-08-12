package com.placebo.codethatidontknowwhatitdoes

import com.placebo.Useless.modules
import com.placebo.Useless.uselessclasses.coide
import net.minecraft.client.Minecraft


class sunnyday: coide("sunnyday") {
override var state = false
    val mc = Minecraft.getInstance()


    override fun Tiick() {

        if(state==true){
           mc.options.gamma().set(0.0)
            println("its so bright that my eyes are fucking burning")
        }
    }
}