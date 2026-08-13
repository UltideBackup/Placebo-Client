package com.placebo.codethatidontknowwhatitdoes

import com.mojang.authlib.minecraft.report.AbuseReport.chat
import com.placebo.Useless.uselessclasses.coide
import net.minecraft.client.Minecraft
import kotlin.random.Random

class autobighit: coide("autobighit") {
    override var state = false
    val mc = Minecraft.getInstance()
    override var category = "Combat"

    override fun Tiick() {
        if (state == true) {
            if (mc.player?.swinging == true) {
                mc.options.keyShift.isDown = true//beautifill architecture for my big hits that bypiss hypix
                mc.options.keyLeft.isDown = true
            }
        }
    }
}