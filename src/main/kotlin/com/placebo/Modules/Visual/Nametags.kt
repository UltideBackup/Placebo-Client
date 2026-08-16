package com.placebo.Modules.Visual
import com.llamalad7.mixinextras.sugar.Local
import com.placebo.Core.Classes.Module
import com.placebo.Utils.makeBox
import com.placebo.Utils.makeName
import com.placebo.Utils.rainbow
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Mob

class Nametags: Module("Nametags") {
    override var state = false
    override var category = "Visual"
    var doDistance = true//make this customizable with settings
    var mc = Minecraft.getInstance()
    override fun Tick() {
        if (state) {
            var level = mc.level
            var player = mc.player
            if (level != null && player != null) {
                for (entity in level.entitiesForRendering()) {
                    var name = entity.name.string
                    var x = entity.x
                    var y = entity.boundingBox.maxY + 0.5
                    var z = entity.z
                    var distance = " ${player.distanceTo(entity).toInt().toString()}m"
                    if (entity !is LocalPlayer && entity !is Mob && doDistance == true) {
                        makeName(x,y,z,name,rainbow(),distance)
                    }else if (entity !is LocalPlayer && entity !is Mob && doDistance == false){
                        makeName(x,y,z,name,rainbow(), null)
                    }

                }
            }


        }
    }
}