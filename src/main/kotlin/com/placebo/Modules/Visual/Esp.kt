package com.placebo.Modules.Visual
import com.placebo.Core.Classes.Module
import com.placebo.Utils.makeBox
import com.placebo.Utils.rainbow
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer

class Esp: Module("Esp") {
    override var state = false
    override var category = "Visual"
    var mc = Minecraft.getInstance()
    override fun Tick() {
        if (state) {
            var level = mc.level
            if (level != null)
                for (entity in level.entitiesForRendering()) {
                    var box = entity.boundingBox
                    if (entity !is LocalPlayer) {makeBox(box, rainbow(), false)}

                }
        }


    }
}