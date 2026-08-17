import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import kotlin.math.exp

fun smoothLookAt(
    player: LocalPlayer,
    targetPos: Vec3,
    smoothness: Float = 15.0f,
    speedMultiplier: Float = 2.0f
) {
    val mc = Minecraft.getInstance()
    val currentX = player.xRot
    val currentY = player.yRot

    player.lookAt(EntityAnchorArgument.Anchor.EYES, targetPos)
    val targetX = player.xRot
    val targetY = player.yRot

    val deltaTicks = mc.deltaTracker.getGameTimeDeltaTicks()
    val dt = (if (deltaTicks <= 0f) 1.0f else deltaTicks) / 20.0f

    val effectiveSmoothness = smoothness * speedMultiplier
    val alpha = (1.0f - exp((-effectiveSmoothness * dt).toDouble()).toFloat()).coerceIn(0.0f, 1.0f)

    val yawDelta = Mth.wrapDegrees(targetY - currentY)
    val pitchDelta = targetX - currentX

    player.xRotO = currentX
    player.yRotO = currentY

    player.xRot = currentX + (pitchDelta * alpha)
    player.yRot = Mth.wrapDegrees(currentY + (yawDelta * alpha))
}

fun smoothSetPitch(
    player: LocalPlayer,
    targetPitch: Float,
    smoothness: Float = 15.0f,
    speedMultiplier: Float = 2.0f
) {
    val mc = Minecraft.getInstance()
    val currentX = player.xRot
    val targetX = targetPitch.coerceIn(-90.0f, 90.0f)

    val deltaTicks = mc.deltaTracker.getGameTimeDeltaTicks()
    val dt = (if (deltaTicks <= 0f) 1.0f else deltaTicks) / 20.0f

    val effectiveSmoothness = smoothness * speedMultiplier
    val alpha = (1.0f - exp((-effectiveSmoothness * dt).toDouble()).toFloat()).coerceIn(0.0f, 1.0f)

    val pitchDelta = targetX - currentX

    player.xRotO = currentX
    player.xRot = currentX + (pitchDelta * alpha)
}