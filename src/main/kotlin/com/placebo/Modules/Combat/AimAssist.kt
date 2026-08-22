package com.placebo.Modules.Combat

import com.placebo.Core.Classes.Module
import com.placebo.Core.Event
import com.placebo.Core.Listener
import com.placebo.Events.RotationEvent
import com.placebo.Utils.DelayHelper
import com.placebo.Utils.makeCircle
import com.placebo.Utils.rainbow
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import smoothLookAt
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.random.Random

class AimAssist: Module("AimAssist"), Listener {
    override var state = false
    override var category = "Combat"
    var mc = Minecraft.getInstance()
    var target: Player? = null
    private val resetDelay = DelayHelper()
    override fun onEvent(event: Event) {
        val player = mc.player ?: return
        val level = mc.level ?: return

        if (!state) return
        resetDelay.tick()
        if (resetDelay.isPending()) return
        if (event is RotationEvent) {
            var closest: Player? = null
            var closestAngle = 360f

            for (entity in level.entitiesForRendering()) {
                if (entity is Player && entity !is LocalPlayer) {
                    val eye = player.eyePosition
                    val center = entity.boundingBox.center
                    val dx = center.x - eye.x
                    val dz = center.z - eye.z
                    val yaw = Math.toDegrees(atan2(dz, dx)).toFloat() - 90f
                    val angle = kotlin.math.abs(Mth.wrapDegrees(player.yRot - yaw))

                    if (angle < closestAngle && player.distanceToSqr(entity) <= 20.25) {
                        closestAngle = angle
                        closest = entity
                    }
                    if (mc.options.keyAttack.isDown == true) {
                        mc.gameMode?.attack(player, entity)
                        player.swing(InteractionHand.MAIN_HAND)
                        mc.options.keyAttack.isDown = false
                    }

                }

                target = closest

                if (target != null) {
                    val eye = player.eyePosition
                    val center = target!!.boundingBox.center
                    val dx = center.x - eye.x
                    val dy = center.y - eye.y
                    val dz = center.z - eye.z
                    val horizontal = hypot(dx, dz)

                    event.yaw = Math.toDegrees(atan2(dz, dx)).toFloat() - 90f
                    event.pitch = -Math.toDegrees(atan2(dy, horizontal)).toFloat()
                    makeCircle(entity.position(), 1f, rainbow(),false)
                }
            }
        }
    }
}