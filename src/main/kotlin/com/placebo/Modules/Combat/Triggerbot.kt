package com.placebo.Modules.Combat

import com.placebo.Core.Classes.Module
import com.placebo.Utils.DelayHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import kotlin.random.Random

class Triggerbot: Module("TriggerBot") {
    override var mode = 0
    override var state = false
    override var category = "Combat"
    private var mc = Minecraft.getInstance()
    private val resetDelay = DelayHelper()
    var from = 60
    var until= 100
    override fun Tick() {
        var player = mc.player ?: return
        var level = mc.level ?: return
        resetDelay.tick()
        if (resetDelay.isPending()) return
        if (!state) return
        var target = mc.crosshairPickEntity
        var gamemode = mc.gameMode
        for (entity in level.entitiesForRendering()) {
            if (entity is Player && entity !is LocalPlayer && entity == target) {
                resetDelay.runAfter(Random.nextLong(from.toLong(),until.toLong())) {
                    gamemode?.attack(player, target)
                    player.swing(InteractionHand.MAIN_HAND)

                }
            }
        }
    }
}