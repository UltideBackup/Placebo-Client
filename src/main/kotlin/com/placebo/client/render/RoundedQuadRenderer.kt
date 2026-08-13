package com.placebo.client.render

import net.minecraft.client.gui.GuiGraphicsExtractor
import kotlin.math.cos
import kotlin.math.sin

/**
 * Extension function for GuiGraphicsExtractor with explicit corner selection.
 */
fun GuiGraphicsExtractor.drawRoundedRect(
    x: Float,
    y: Float,
    w: Float,
    h: Float,
    radius: Float,
    color: Int,
    roundTopLeft: Boolean = true,
    roundTopRight: Boolean = true,
    roundBottomLeft: Boolean = true,
    roundBottomRight: Boolean = true
) {
    if (w <= 0f || h <= 0f) return

    val maxAllowedRadius = (w / 2f).coerceAtMost(h / 2f)
    val rad = radius.coerceAtMost(maxAllowedRadius)

    val ix = x.toInt()
    val iy = y.toInt()
    val iw = w.toInt()
    val ih = h.toInt()
    val ir = rad.toInt()

    if (ir <= 0) {
        fill(ix, iy, ix + iw, iy + ih, color)
        return
    }

    // Main central fills
    fill(ix + ir, iy, ix + iw - ir, iy + ih, color)
    fill(
        ix,
        iy + (if (roundTopLeft || roundTopRight) ir else 0),
        ix + ir,
        iy + ih - (if (roundBottomLeft || roundBottomRight) ir else 0),
        color
    )
    fill(
        ix + iw - ir,
        iy + (if (roundTopLeft || roundTopRight) ir else 0),
        ix + iw,
        iy + ih - (if (roundBottomLeft || roundBottomRight) ir else 0),
        color
    )

    // Corner 1: Top-Right
    if (roundTopRight) {
        drawCornerArc(this, ix + iw - ir, iy + ir, rad, 270.0, 360.0, 8, color)
    } else {
        fill(ix + iw - ir, iy, ix + iw, iy + ir, color)
    }

    // Corner 2: Top-Left
    if (roundTopLeft) {
        drawCornerArc(this, ix + ir, iy + ir, rad, 180.0, 270.0, 8, color)
    } else {
        fill(ix, iy, ix + ir, iy + ir, color)
    }

    // Corner 3: Bottom-Left
    if (roundBottomLeft) {
        drawCornerArc(this, ix + ir, iy + ih - ir, rad, 90.0, 180.0, 8, color)
    } else {
        fill(ix, iy + ih - ir, ix + ir, iy + ih, color)
    }

    // Corner 4: Bottom-Right
    if (roundBottomRight) {
        drawCornerArc(this, ix + iw - ir, iy + ih - ir, rad, 0.0, 90.0, 8, color)
    } else {
        fill(ix + iw - ir, iy + ih - ir, ix + iw, iy + ih, color)
    }
}

private fun drawCornerArc(
    context: GuiGraphicsExtractor,
    centerX: Int,
    centerY: Int,
    radius: Float,
    startAngleDeg: Double,
    endAngleDeg: Double,
    segments: Int,
    color: Int
) {
    val step = (endAngleDeg - startAngleDeg) / segments
    for (i in 0 until segments) {
        val a1 = Math.toRadians(startAngleDeg + i * step)
        val a2 = Math.toRadians(startAngleDeg + (i + 1) * step)

        val x1 = (centerX + cos(a1) * radius).toInt()
        val y1 = (centerY + sin(a1) * radius).toInt()
        val x2 = (centerX + cos(a2) * radius).toInt()
        val y2 = (centerY + sin(a2) * radius).toInt()

        val minX = minOf(centerX, x1, x2)
        val maxX = maxOf(centerX, x1, x2)
        val minY = minOf(centerY, y1, y2)
        val maxY = maxOf(centerY, y1, y2)

        context.fill(minX, minY, maxX, maxY, color)
    }
}