package com.placebo.Modules.Combat

import com.placebo.Core.Classes.Module
import com.placebo.Utils.DelayHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import kotlin.random.Random

class AntiMaceSmash: Module("Anti Mace Smash") {
    override var mode = 0
    override var state = false
    override var category = "Combat"
    var mc = Minecraft.getInstance()
    private val resetDelay = DelayHelper()
    override fun Tick() {
        var level = mc.level?:return
        var player = mc.player?:return
        resetDelay.tick()
        if (resetDelay.isPending()) return
        if (state) {
            for (entity in level.entitiesForRendering()) {
                if (entity is Player && entity !is LocalPlayer) {
                    if (entity.mainHandItem.item == Items.MACE) {
                        var yDif = kotlin.math.abs(entity.y - player.y)
                        if (yDif >=5.0) {
                            mc.options.keyUp.isDown = true
                            player.isSprinting = true
                            resetDelay.runAfter(Random.nextLong(30,70)) {
                                mc.options.keyUp.isDown = false
                                player.isSprinting = false
                            }
                        }
                    }
                }
            }
        }
    }
}