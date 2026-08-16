package com.placebo.Modules.Movement

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft

class AirJump: Module("Air Jump") {
    override var state=false
    override var category="Movement"
    var mc = Minecraft.getInstance()
    private var wasJumping=false


    override fun Tick() {
        var jumping= mc.options.keyJump.isDown
        if (state && jumping && !wasJumping){

    mc.player?.jumpFromGround()
}
        wasJumping=jumping
        }
    }
