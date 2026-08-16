package com.placebo.Modules.Movement

import com.placebo.Core.Classes.Module
import com.placebo.Core.Event
import com.placebo.Core.Listener
import com.placebo.Events.StepEvent
import net.minecraft.client.Minecraft

class Step: Module("Step"), Listener {
    override var state=false
    override var category="Movement"
    var mc = Minecraft.getInstance()
    override fun onEvent(event: Event) {
        if(state && event is StepEvent) {
            event.StepHeight = 1.0f
        }
    }
}