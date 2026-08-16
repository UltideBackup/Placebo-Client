package com.placebo.Modules.Combat

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.Items

class AutoSwap: Module("Auto Swap") {
    override var mode = 0
    override var state = false
    override var category = "Combat"
    var mc = Minecraft.getInstance()


    override fun Tick() {
        var player = mc.player ?: return
        var level = mc.level ?: return
        var gamemode = mc.gameMode ?: return
        if (state) {
            for (entity in level.entitiesForRendering()) {
                if (mc.crosshairPickEntity == entity && entity is Player && entity !is LocalPlayer) {
                    for (i in 0..8) {
                        var item = player.inventory.getItem(i)
                        if (entity.isBlocking && item.item is AxeItem) {
                            player.inventory.selectedSlot = i
                            break
                        }
                        if (entity.isBlocking == false && item.`is`(ItemTags.SWORDS)) {//pretty sure we can just use == itemtag without all this is shit but thats ok
                            player.inventory.selectedSlot = i
                            break
                        }
                        if (player.distanceToSqr(entity) <= 576.0) {
                            if (item.item == Items.BOW || item.item == Items.CROSSBOW) {
                                player.inventory.selectedSlot = i
                                break
                            }

                        }
                    }
                }
            }
        }
    }
}