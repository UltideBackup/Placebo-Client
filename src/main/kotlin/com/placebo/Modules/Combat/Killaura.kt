package com.placebo.Modules.Combat

import com.placebo.Core.Classes.Module
import com.placebo.Utils.makeCircle
import com.placebo.Utils.rainbow
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

class Killaura: Module("Killaura") {
    override var state=false
    override var category="Combat"
    override var mode=0
    var mc = Minecraft.getInstance()
    override fun Tick() {
        if (!state) return
        when(mode){
            0 -> AttackWithoutRotation()
            1 -> AttackWithRotation()
        }
    }

    fun AttackWithoutRotation(){
        var player = mc.player?:return
        var level = mc.level?:return
        var gamemode = mc.gameMode
        for (entity in level.entitiesForRendering()){
            if (entity is Player&& entity !is LocalPlayer){
                gamemode?.attack(player, entity)
                player.swing(InteractionHand.MAIN_HAND)
                makeCircle(entity.position(), 1f, rainbow(),false)
                break
            }
        }

    }

    fun AttackWithRotation(){
        var player = mc.player?:return
        var level = mc.level?:return
        var gamemode = mc.gameMode
        for (entity in level.entitiesForRendering()){
            if (entity is Player&& entity !is LocalPlayer){
               var  entityX=entity.x
                var entityY=entity.y+1
                var entityZ=entity.z
                player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3(entityX, entityY, entityZ))
                gamemode?.attack(player, entity)
                player.swing(InteractionHand.MAIN_HAND)
                makeCircle(entity.position(), 1f, rainbow(),false)
                break
            }


        }

    }

}