package com.placebo.Modules.Movement

import com.placebo.Core.Classes.Module
import com.placebo.Core.Event
import com.placebo.Core.Listener
import com.placebo.Events.LedgeEvent
import net.minecraft.client.Minecraft

class SafeWalk: Module("SafeWalk"), Listener {
    override var state=false
    override var category="Movement"
    var mc = Minecraft.getInstance()
    override fun onEvent(event: Event) {
        if (!state) return
        if (event is LedgeEvent){
            event.clip = true
        }
    }
    }