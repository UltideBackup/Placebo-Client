package com.placebo.Modules.Joke

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.world.phys.Vec3
import kotlin.random.Random

class Jokebackknocking: Module("backknocking") {//antikb
    override var state = false
    val mc = Minecraft.getInstance()
    override var category = "Joke"


    override fun Tick() {

if(state==true && mc.player != null){
    val player = mc.player?:return

if(player.swinging == true){
    mc.options.keyAttack.isDown = false
    mc.options.keyDown.isDown = true

    player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3(Random.nextInt(-1000000000,1000000000).toDouble(),Random.nextInt(-1000000000,1000000000).toDouble(),Random.nextInt(-1000000000,1000000000).toDouble()))
}
}
    }
}