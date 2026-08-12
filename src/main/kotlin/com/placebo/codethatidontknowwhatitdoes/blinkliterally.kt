package com.placebo.codethatidontknowwhatitdoes

import com.placebo.Useless.uselessclasses.coide
import net.minecraft.client.Minecraft
import kotlin.random.Random

class blinkliterally: coide("blinkliterally") {
override var state = false
    val mc = Minecraft.getInstance()


    override fun Tiick() {
        if(state==true){
            var chance = Random.nextInt(1,100)
            if (chance >=75){
                println("im not making this LMAO(bc of new mappings i cant easily makea gui)")
            }
            }
        }
    }
