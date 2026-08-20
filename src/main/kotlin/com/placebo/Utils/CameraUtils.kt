import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.exp
import kotlin.math.hypot
import kotlin.math.roundToInt

private var targetYaw = 0f
private var targetPitch = 0f

private var rotationYaw = 0f
private var rotationPitch = 0f

private var previousRotationYaw = 0f
private var previousRotationPitch = 0f

private var previousTargetYaw = 0f
private var previousTargetPitch = 0f

private var initialized = false

private val mc = Minecraft.getInstance()

private fun gcd(): Float {
    val sens = mc.options.sensitivity().get()
    val f = sens * 0.6f + 0.2f
    return (f * f * f * 1.2f).toFloat()
}

private fun normalizeAngle(angle: Float): Float {
    return (angle / 180f).coerceIn(0f, 1f)
}

private fun normalizeRotation(angle: Float, current: Float): Float {
    val g = gcd()
    val delta = Mth.wrapDegrees(angle - current)
    val rounded = (delta / g).roundToInt() * g
    return Mth.wrapDegrees(current + rounded)
}

private fun sigmoid(t: Float): Float {
    return 1f / (1f + exp((-0.5f * (t - 0.3f)).toDouble()).toFloat())
}

private fun bezier(start: Float, end: Float, t: Float): Float {
    return (1f - t) * (1f - t) * start + 2f * (1f - t) * t * 1f + t * t * end
}

private fun calculateFactor(
    difference: Float,
    speed: Float,
    directionChange: Float,
    midpoint: Float
): Float {
    val t = normalizeAngle(difference)

    val bezierSpeed = bezier(0.05f, 1f, 1f - t)
    val sigmoidSpeed = sigmoid(t)

    return if (t > midpoint) {
        bezierSpeed * speed
    } else {
        sigmoidSpeed * (speed + directionChange).coerceIn(0f, 1f)
    }
}

private fun stepRotation(
    current: Float,
    target: Float,
    speed: Float,
    directionChange: Float,
    midpoint: Float,
    wrap: Boolean
): Float {
    val delta = if (wrap) {
        Mth.wrapDegrees(target - current)
    } else {
        target - current
    }

    val difference = abs(delta)

    if (difference < 0.001f) {
        return target
    }

    val factor = calculateFactor(
        difference,
        speed,
        directionChange,
        midpoint
    )

    return if (wrap) {
        Mth.wrapDegrees(current + delta * factor)
    } else {
        (current + delta * factor).coerceIn(-90f, 90f)
    }
}

fun resetSmoothRotation(player: LocalPlayer) {
    targetYaw = player.yRot
    targetPitch = player.xRot

    rotationYaw = player.yRot
    rotationPitch = player.xRot

    previousRotationYaw = player.yRot
    previousRotationPitch = player.xRot

    previousTargetYaw = player.yRot
    previousTargetPitch = player.xRot

    initialized = true
}

fun smoothLookAt(
    player: LocalPlayer,
    pos: Vec3,
    horizontalSpeed: Float = 82f,
    verticalSpeed: Float = 22f,
    directionChangeFactor: Float = 97f,
    midpoint: Float = 0.35f
) {
    if (!initialized) {
        resetSmoothRotation(player)
    }

    val eye = player.getEyePosition()

    val dx = pos.x - eye.x
    val dy = pos.y - eye.y
    val dz = pos.z - eye.z

    val horizontal = hypot(dx, dz)

    val newTargetYaw = Math.toDegrees(atan2(dz, dx)).toFloat() - 90f
    val newTargetPitch = -Math.toDegrees(atan2(dy, horizontal)).toFloat().coerceIn(-90f, 90f)

    // direction change: how much the TARGET moved since last tick
    val targetDeltaYaw = abs(Mth.wrapDegrees(newTargetYaw - targetYaw))
    val targetDeltaPitch = abs(newTargetPitch - targetPitch)

    targetYaw = newTargetYaw
    targetPitch = newTargetPitch

    previousRotationYaw = rotationYaw
    previousRotationPitch = rotationPitch

    val directionChange = normalizeAngle(
        hypot(targetDeltaYaw.toDouble(), targetDeltaPitch.toDouble()).toFloat()
    ) * (directionChangeFactor / 100f)

    rotationYaw = stepRotation(
        rotationYaw,
        targetYaw,
        horizontalSpeed / 100f,
        directionChange,
        midpoint,
        true
    )

    rotationPitch = stepRotation(
        rotationPitch,
        targetPitch,
        verticalSpeed / 100f,
        directionChange,
        midpoint,
        false
    )

    // GCD normalize
    rotationYaw = normalizeRotation(rotationYaw, previousRotationYaw)
    rotationPitch = normalizeRotation(rotationPitch, previousRotationPitch)

    applyInterpolatedRotation(player)
}

private fun applyInterpolatedRotation(player: LocalPlayer) {
    val partialTicks = mc.deltaTracker.getGameTimeDeltaTicks().coerceIn(0f, 1f)

    val visualYaw = Mth.wrapDegrees(
        previousRotationYaw + Mth.wrapDegrees(rotationYaw - previousRotationYaw) * partialTicks
    )

    val visualPitch = previousRotationPitch + (rotationPitch - previousRotationPitch) * partialTicks

    player.yRotO = player.yRot
    player.xRotO = player.xRot

    player.yRot = visualYaw
    player.xRot = visualPitch.coerceIn(-90f, 90f)
}

fun smoothSetPitch(
    player: LocalPlayer,
    target: Float,
    verticalSpeed: Float = 22f,
    directionChangeFactor: Float = 97f,
    midpoint: Float = 0.35f
) {
    if (!initialized) {
        resetSmoothRotation(player)
    }

    val newTargetPitch = target.coerceIn(-90f, 90f)
    val targetDeltaPitch = abs(newTargetPitch - targetPitch)

    targetPitch = newTargetPitch

    previousRotationPitch = rotationPitch

    val directionChange = normalizeAngle(abs(targetDeltaPitch)) * (directionChangeFactor / 100f)

    rotationPitch = stepRotation(
        rotationPitch,
        targetPitch,
        verticalSpeed / 100f,
        directionChange,
        midpoint,
        false
    )

    rotationPitch = normalizeRotation(rotationPitch, previousRotationPitch)

    applyInterpolatedRotation(player)
}