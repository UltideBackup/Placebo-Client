package com.placebo.codethatidontknowwhatitdoes

import com.placebo.Useless.uselessclasses.coide
import net.minecraft.client.Minecraft
import net.minecraft.world.item.ItemStack
import kotlin.random.Random

class fuckingstopmefromthetortureofmakingthis: coide("autototem") {
    override var state = true
    val mc = Minecraft.getInstance()

    override fun Tiick() {
        var inv = mc.player?.inventory
        if (state == true && inv != null){
          inv.selectedSlot = Random.nextInt(0, 9)
            var from = inv.selectedSlot
            val to = 40
            var item = inv.getItem(to)
            if (true){
               inv.setItem(to, item)
                inv.setItem(from, ItemStack.EMPTY)
            }
        }
    }
}