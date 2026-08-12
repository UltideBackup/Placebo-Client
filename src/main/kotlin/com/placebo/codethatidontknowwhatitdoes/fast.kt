package com.placebo.codethatidontknowwhatitdoes

import com.placebo.Useless.uselessclasses.coide
import net.minecraft.client.Minecraft
import kotlin.random.Random

class fast: coide("fast") {
    override var state = false
    val mc = Minecraft.getInstance()

    override fun Tiick() {
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