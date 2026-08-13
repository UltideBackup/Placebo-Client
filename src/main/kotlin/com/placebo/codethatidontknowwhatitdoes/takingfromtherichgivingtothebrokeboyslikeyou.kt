package com.placebo.codethatidontknowwhatitdoes

import com.placebo.Useless.uselessclasses.coide
import net.minecraft.client.Minecraft
import net.minecraft.world.level.block.ChestBlock
import net.minecraft.world.level.block.entity.ChestBlockEntity

class steel: coide("steel") {
    override var state = false
    val mc = Minecraft.getInstance()
    override var category = "World"

    override fun Tiick() {
        val player = mc.player ?: return
        val inv = player.inventory
        val selected = inv.selectedSlot
        if (state == true) {
            for (i in 0..40) {
                println("steeling bad no good")
                if (i == selected) {
                    if (!inv.getItem(selected).isEmpty) {
                        player.drop(true)
                    }
                    continue
                }

                val stackToDrop = inv.getItem(i)
                if (stackToDrop.isEmpty) continue

                val oldHandStack = inv.getItem(selected)
                inv.setItem(selected, stackToDrop)
                inv.setItem(i, oldHandStack)

                player.drop(true)
            }//gemini basically made 60% of this i spent like 30 minutes trying and eventually gave up
        }
    }
}
