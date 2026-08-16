package com.placebo.Modules.Combat

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import kotlin.random.Random

class Triggerbot: Module("TriggerBot") {
    override var mode=0
    override var state=false
    override var category="Combat"
private var mc = Minecraft.getInstance()
   private  var attackdelay = 0
    private var hadtarget = false
    private var hitdelay = 0
    override fun Tick() {
        var player = mc.player ?: return
        var target = mc.crosshairPickEntity
        var gamemode=mc.gameMode
        var falling = !player.onGround()&& player.deltaMovement.y() < 0
        val validtarget = target != null && target is Player && state

        if (!validtarget) {
            hadtarget=false
            attackdelay=0
            hitdelay=0
            return
        }

        if(!hadtarget){
            hadtarget=true
            attackdelay=  Random.nextInt(2,5)
        }



if (attackdelay>0){
    attackdelay--
    return
}

        if (hitdelay>0){
            hitdelay--
            return
        }
        gamemode?.attack(player, target)
        player.swing(InteractionHand.MAIN_HAND)
        hitdelay= Random.nextInt(2,5)



    }
}