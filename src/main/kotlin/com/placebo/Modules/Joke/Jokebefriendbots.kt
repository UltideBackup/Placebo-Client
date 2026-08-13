package com.placebo.Modules.Joke

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.player.Player

class Jokebefriendbots: Module("befriendbots") {
    override var state = false
    var mc = Minecraft.getInstance()
    override var category = "Joke"

    override fun Tick() {
var level = mc.level
        if (state == true && level != null) {

            for (player in level.entitiesForRendering()) {
                if (player is Player && player !is LocalPlayer) {
                    var name = player.name//i coudlnt be bothered to strip it for just the name
                    mc.connection?.sendChat("fuck you $name you fucking bot i hate you bitch")
                }
            }
        }
    }
}