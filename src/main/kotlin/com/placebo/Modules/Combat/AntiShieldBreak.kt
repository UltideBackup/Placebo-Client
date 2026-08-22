package com.placebo.Modules.Combat

import com.placebo.Core.Classes.Module
import com.placebo.Utils.DelayHelper
import com.placebo.Utils.getEnchantLevel
import com.placebo.Utils.makeCircle
import com.placebo.Utils.rainbow
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.tags.ItemTags
import kotlin.random.Random


//holy shit this is bad but it should work
class AntiShieldBreak: Module("Anti Shield Break") {
    override var mode = 0
    override var state = false
    override var category = "Combat"
    var mc = Minecraft.getInstance()
    private val resetDelay = DelayHelper()


    override fun Tick() {
        var player = mc.player ?: return
        var level = mc.level ?: return
        var gamemode = mc.gameMode ?: return
        if (!state) return
        resetDelay.tick()
        if (resetDelay.isPending()) return
        for (entity in level.entitiesForRendering()) {
            if (player.isBlocking && mc.crosshairPickEntity == entity) {
                var bestslot = -1
                var highestkb = 0
                if (entity is Player && entity !is LocalPlayer && entity.mainHandItem.item is AxeItem) {
                    makeCircle(entity.position(), 1f, rainbow(),false)
                    for (i in 0..8) {
                        var item = player.inventory.getItem(i)
                        val kb = item.getEnchantLevel(Enchantments.KNOCKBACK)
                        if (kb > highestkb) {
                            highestkb = kb
                            bestslot = i
                        } else if (highestkb == 0 && bestslot == -1 && item.`is`(ItemTags.SWORDS)) {
                            bestslot = i

                        }


                    }
                    if (bestslot != -1) {
                        val oldSlot = player.inventory.selectedSlot
                        mc.options.keyUse.isDown = false
                        gamemode.releaseUsingItem(player)
                        resetDelay.runAfter(Random.nextInt(40, 90).toLong()) {
                            player.inventory.selectedSlot = bestslot

                            resetDelay.runAfter(Random.nextInt(30, 70).toLong()) {
                                if (entity.isAlive) {
                                    gamemode.attack(player, entity)
                                    player.swing(InteractionHand.MAIN_HAND)
                                }

                                resetDelay.runAfter(Random.nextInt(300, 550).toLong()) {
                                    player.inventory.selectedSlot = oldSlot
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}