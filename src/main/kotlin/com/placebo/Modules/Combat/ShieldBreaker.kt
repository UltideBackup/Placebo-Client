package com.placebo.Modules.Combat

import com.placebo.Core.Classes.Module
import com.placebo.Utils.DelayHelper
import com.placebo.Utils.makeCircle
import com.placebo.Utils.rainbow
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.AxeItem
import kotlin.random.Random

class ShieldBreaker: Module("Shield Breaker") {
    override var mode=0
    override var state=false
    override var category="Combat"
    var mc = Minecraft.getInstance()
    var oldSlot = -1
    private val resetDelay = DelayHelper()
//bro this shit is deadass yandere sim
    override fun Tick() {
        var player = mc.player ?: return
        var level = mc.level?:return
    var gamemode = mc.gameMode?:return
        if (!state) return
    resetDelay.tick()
    if (resetDelay.isPending()) return
        for (entity in level.entitiesForRendering()){
            if (entity is Player && entity !is LocalPlayer){
                if (entity.isBlocking && mc.crosshairPickEntity == entity){
                    makeCircle(entity.position(), 1f, rainbow(),false)
                    for (i in 0..8){
                        var item = player.inventory.getItem(i)
                        if (item.item is AxeItem){
                            oldSlot = player.inventory.selectedSlot
                            resetDelay.runAfter(Random.nextLong(40,90)) {
                                player.inventory.selectedSlot = i

                                resetDelay.runAfter(Random.nextLong(30, 70)) {
                                    gamemode.attack(player, entity)
                                    player.swing(net.minecraft.world.InteractionHand.MAIN_HAND)
                                    resetDelay.runAfter(Random.nextInt(300, 500).toLong()) {
                                        player.inventory.selectedSlot = oldSlot
                                    }
                                }
                            }
                            break
                        }
                    }
                }
            }
        }
    }
}