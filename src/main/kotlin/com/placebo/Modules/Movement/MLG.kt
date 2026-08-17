package com.placebo.Modules.Movement

import com.placebo.Core.Classes.Module
import com.placebo.Utils.DelayHelper
import net.minecraft.client.Minecraft
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.Items
import smoothSetPitch
import kotlin.random.Random

class MLG : Module("Mlg") {
    override var state = false
    override var category = "Movement"
    private val mc = Minecraft.getInstance()
    private val resetDelay = DelayHelper()
    private var bucketSlot = -1
    private var lookingDown = false

    override fun Tick() {
        val player = mc.player ?: return
        if (!state) return

        resetDelay.tick()
        if (resetDelay.isPending()) return

        if (player.fallDistance <= 3.0 || player.deltaMovement.y >= -0.5) {
            bucketSlot = -1
            lookingDown = false
            return
        }

        if (bucketSlot == -1) {
            for (i in 0..8) {
                if (player.inventory.getItem(i).item == Items.WATER_BUCKET) {
                    bucketSlot = i
                    break
                }
            }
            if (bucketSlot == -1) return
        }

        if (player.inventory.selectedSlot != bucketSlot) {
            player.inventory.selectedSlot = bucketSlot
            return
        }

        if (!lookingDown) {
            smoothSetPitch(player, 90f, 10f, 20f)
            lookingDown = true
        }

        val gamemode = mc.gameMode ?: return
        gamemode.useItem(player, InteractionHand.MAIN_HAND)

        resetDelay.runAfter(Random.nextInt(8, 18).toLong()) {
            bucketSlot = -1
            lookingDown = false
        }
    }
}