package com.placebo.Modules.Movement
import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3

class Scaffold:Module("Scaffold") {
    override var state=false
    override var category="Movement"
    var mc = Minecraft.getInstance()
    override fun Tick() {
        var player = mc.player ?: return
        var level = mc.level ?: return
        if (state) {
            var blockunder = player.blockPosition().below()
var gamemode = mc.gameMode
            val hitVec = Vec3(
                blockunder.x + 0.5,
                blockunder.y + 1.0,
                blockunder.z + 0.5
            )
            val hitResult = BlockHitResult(
                hitVec,
                Direction.UP,
                blockunder,
                false
            )

            gamemode?.useItemOn(
                player,
                InteractionHand.MAIN_HAND,
                hitResult
            )
        }
    }
}