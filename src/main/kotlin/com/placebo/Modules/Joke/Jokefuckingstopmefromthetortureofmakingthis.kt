package com.placebo.Modules.Joke

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft
import net.minecraft.world.item.ItemStack
import kotlin.random.Random

class Jokefuckingstopmefromthetortureofmakingthis: Module("autototem") {
    override var state = false
    val mc = Minecraft.getInstance()
    override var category = "Joke"
    override fun Tick() {
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