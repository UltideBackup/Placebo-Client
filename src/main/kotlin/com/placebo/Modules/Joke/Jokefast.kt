package com.placebo.Modules.Joke

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft
import kotlin.random.Random

class Jokefast: Module("fast") {
    override var state = false
    val mc = Minecraft.getInstance()
    override var category = "Joke"

    override fun Tick() {
      if (state == true){
          mc.options.fov().set(110)//bpass hpixel
         var chance = Random.nextInt(1,50)//2% chance for hypersonic speed
          if (chance == 37){
              mc.options.fov().set(45)//make it so you bypass by looking legit
          }
          mc.player?.inventory?.selectedSlot = Random.nextInt(0,8)//so fast that it changes your hotbar slot doesnt effect gameplay though
          print("fast bypass suscess")
      }
            }
}