package com.placebo.Modules.World

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft

class FastPlace: Module("Fast Place")  {
    override var category="World"
    override var state=false
    var mc = Minecraft.getInstance()
    override fun Tick() {
        if (state) {
mc.rightClickDelay = 0
        }
    }
}