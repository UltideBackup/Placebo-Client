package com.placebo.Modules.Joke

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.world.phys.Vec3
import kotlin.random.Random

class Jokekillaura: Module("killaura") {
    override var state = false
    val mc = Minecraft.getInstance()
    override var category = "Joke"

    override fun Tick() {
        var level = mc.level?:return
        var player = mc.player
        if (state == true) {
            for (entities in level.entitiesForRendering()) {
                var x = entities.x//useless bc we only like our random ones
                var y = entities.y
                var z = entities.z
                player?.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3(Random.nextInt(1,1000).toDouble(), Random.nextInt(1,1000).toDouble(), Random.nextInt(1,1000).toDouble()))
                mc.options.fov().set(Random.nextInt(30,110))
                mc.options.keyAttack.isDown = true
                mc.options.keyAttack.isDown = false
            }
        }
    }
}