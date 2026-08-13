package com.placebo.Modules.Joke

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft
import kotlin.random.Random

class Jokeblinkliterally: Module("blinkliterally") {
override var state = false
    val mc = Minecraft.getInstance()
    override var category = "Joke"


    override fun Tick() {
        if(state==true){
            var chance = Random.nextInt(1,100)
            if (chance >=75){
                println("im not making this LMAO(bc of new mappings i cant easily makea gui)")
            }
            }
        }
    }
