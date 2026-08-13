package com.placebo.client

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.util.ARGB

// ═══════════════════════════════════════════════════════════════════════════
// the clickgui + decoy modules were made by ai i cannot be bothered to fucking make this shit
// ═══════════════════════════════════════════════════════════════════════════

object Theme {

    // ── Monochrome palette ───────────────────────────────────────────────
    const val BACKDROP:   Int = 0xD0000000.toInt()//gray
    const val INK_0:      Int = 0xFF000000.toInt()
    const val INK_25:     Int = 0xFF050505.toInt()
    const val INK_50:     Int = 0xFF0A0A0A.toInt()
    const val INK_75:     Int = 0xFF0F0F0F.toInt()
    const val INK_100:    Int = 0xFF141414.toInt()
    const val INK_125:    Int = 0xFF181818.toInt()
    const val INK_150:    Int = 0xFF1C1C1C.toInt()
    const val INK_175:    Int = 0xFF222222.toInt()
    const val INK_200:    Int = 0xFF282828.toInt()
    const val INK_250:    Int = 0xFF303030.toInt()
    const val INK_300:    Int = 0xFF383838.toInt()
    const val INK_350:    Int = 0xFF404040.toInt()
    const val INK_400:    Int = 0xFF4A4A4A.toInt()
    const val INK_500:    Int = 0xFF5F5F5F.toInt()
    const val INK_550:    Int = 0xFF6F6F6F.toInt()
    const val INK_600:    Int = 0xFF808080.toInt()
    const val INK_650:    Int = 0xFF909090.toInt()
    const val INK_700:    Int = 0xFFA0A0A0.toInt()
    const val INK_750:    Int = 0xFFB0B0B0.toInt()
    const val INK_800:    Int = 0xFFC8C8C8.toInt()
    const val INK_850:    Int = 0xFFD8D8D8.toInt()
    const val INK_900:    Int = 0xFFEAEAEA.toInt()
    const val INK_950:    Int = 0xFFF5F5F5.toInt()
    const val INK_1000:   Int = 0xFFFFFFFF.toInt()//gray and everything fucking else wtf are you doing bro

    // ── Layout (in scaled coords, ~GUI scale 2 equivalent) ───────────────
    const val PANEL_WIDTH:        Int = 178
    const val PANEL_HEADER_H:     Int = 24
    const val MODULE_ROW_H:       Int = 17
    const val MODULE_PAD_X:       Int = 11
    const val PANEL_RADIUS:       Int = 7
    const val MAX_PANEL_BODY_H:   Int = 280

    // ── Color helpers ────────────────────────────────────────────────────
    fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t

    fun lerpColor(a: Int, b: Int, t: Float): Int {
        val c = t.coerceIn(0f, 1f)
        val ar = (a shr 16 and 0xFF) / 255f
        val ag = (a shr 8 and 0xFF) / 255f
        val ab = (a and 0xFF) / 255f
        val aa = (a shr 24 and 0xFF) / 255f
        val br = (b shr 16 and 0xFF) / 255f
        val bg = (b shr 8 and 0xFF) / 255f
        val bb = (b and 0xFF) / 255f
        val ba = (b shr 24 and 0xFF) / 255f
        val r = (lerp(ar, br, c) * 255f).toInt() and 0xFF
        val g = (lerp(ag, bg, c) * 255f).toInt() and 0xFF
        val bl = (lerp(ab, bb, c) * 255f).toInt() and 0xFF
        val al = (lerp(aa, ba, c) * 255f).toInt() and 0xFF
        return (al shl 24) or (r shl 16) or (g shl 8) or bl
    }

    /** Returns color `c` with alpha multiplied by `a` (0..1). */
    fun withAlpha(c: Int, a: Float): Int {
        val alpha = ((c ushr 24 and 0xFF) * a.coerceIn(0f, 1f)).toInt() and 0xFF
        return (alpha shl 24) or (c and 0x00FFFFFF)
    }
}

// ── Anti-aliased rounded rectangle helpers ───────────────────────────────
// Each corner is drawn as a quarter-circle of horizontal 1px slices.
// Edge pixels get partial alpha to anti-alias the curve.
fun GuiGraphicsExtractor.fillRounded(x: Int, y: Int, w: Int, h: Int, r: Int, color: Int) {
    if (w <= 0 || h <= 0) return
    val rad = r.coerceAtLeast(0).coerceAtMost(w / 2).coerceAtMost(h / 2)
    if (rad == 0) {
        fill(x, y, x + w, y + h, color)
        return
    }

    val baseAlpha = (color ushr 24 and 0xFF) / 255f

    // Body (full width, between top and bottom corner strips)
    fill(x, y + rad, x + w, y + h - rad, color)
    // Top strip (between top corners)
    fill(x + rad, y, x + w - rad, y + rad, color)
    // Bottom strip (between bottom corners)
    fill(x + rad, y + h - rad, x + w - rad, y + h, color)

    // 4 corners — each is a quarter-circle of 1px horizontal slices
    for (i in 0 until rad) {
        // Distance from outer edge of the corner to the center of slice i
        val dy = i + 0.5f  // pixel center offset
        val dyFromCenter = rad - dy  // distance from circle center
        // Compute chord width at this y, with anti-aliasing on both edges.
        val r2 = rad * rad
        val dx = Math.sqrt((r2 - dyFromCenter * dyFromCenter).toDouble()).toFloat()

        // Inner edge alpha (fades in as we approach the circle)
        val innerAlpha = (dx / rad).coerceAtMost(1f) * baseAlpha
        // Outer edge alpha (anti-alias the outer rim)
        val outerAlpha = ((dx + 1f) / rad).coerceAtMost(1f) * baseAlpha

        val innerColor = Theme.withAlpha(color, innerAlpha)
        val outerColor = Theme.withAlpha(color, outerAlpha)

        // top-left corner: spans from (x + rad - dx, y + i) to (x + rad, y + i + 1)
        fill((x + rad - dx).toInt(), y + i, x + rad, y + i + 1, innerColor)
        // top-right corner
        fill(x + w - rad, y + i, ((x + w - rad + dx).toInt()), y + i + 1, innerColor)
        // bottom-left corner
        fill((x + rad - dx).toInt(), y + h - rad + i, x + rad, y + h - rad + i + 1, innerColor)
        // bottom-right corner
        fill(x + w - rad, y + h - rad + i, ((x + w - rad + dx).toInt()), y + h - rad + i + 1, innerColor)
    }
}

/** Rounded top corners only (for panel headers). */
fun GuiGraphicsExtractor.fillRoundedTop(x: Int, y: Int, w: Int, h: Int, r: Int, color: Int) {
    if (w <= 0 || h <= 0) return
    val rad = r.coerceAtLeast(0).coerceAtMost(w / 2).coerceAtMost(h)
    if (rad == 0) {
        fill(x, y, x + w, y + h, color)
        return
    }

    val baseAlpha = (color ushr 24 and 0xFF) / 255f

    // Body below the rounded top
    fill(x, y + rad, x + w, y + h, color)
    // Top strip between corners
    fill(x + rad, y, x + w - rad, y + rad, color)

    for (i in 0 until rad) {
        val dy = i + 0.5f
        val dyFromCenter = rad - dy
        val r2 = rad * rad
        val dx = Math.sqrt((r2 - dyFromCenter * dyFromCenter).toDouble()).toFloat()
        val innerAlpha = (dx / rad).coerceAtMost(1f) * baseAlpha
        val innerColor = Theme.withAlpha(color, innerAlpha)
        fill((x + rad - dx).toInt(), y + i, x + rad, y + i + 1, innerColor)
        fill(x + w - rad, y + i, ((x + w - rad + dx).toInt()), y + i + 1, innerColor)
    }
}

/** Rounded bottom corners only. */
fun GuiGraphicsExtractor.fillRoundedBottom(x: Int, y: Int, w: Int, h: Int, r: Int, color: Int) {
    if (w <= 0 || h <= 0) return
    val rad = r.coerceAtLeast(0).coerceAtMost(w / 2).coerceAtMost(h)
    if (rad == 0) {
        fill(x, y, x + w, y + h, color)
        return
    }

    val baseAlpha = (color ushr 24 and 0xFF) / 255f

    // Body above the rounded bottom
    fill(x, y, x + w, y + h - rad, color)
    // Bottom strip between corners
    fill(x + rad, y + h - rad, x + w - rad, y + h, color)

    for (i in 0 until rad) {
        val dx = Math.sqrt((rad * rad - i * i).toDouble()).toFloat()
        val innerAlpha = (dx / rad).coerceAtMost(1f) * baseAlpha
        val innerColor = Theme.withAlpha(color, innerAlpha)
        fill((x + rad - dx).toInt(), y + h - rad + i, x + rad, y + h - rad + i + 1, innerColor)
        fill(x + w - rad, y + h - rad + i, ((x + w - rad + dx).toInt()), y + h - rad + i + 1, innerColor)
    }
}

/** Outline a rounded rectangle with a 1px border. */
fun GuiGraphicsExtractor.outlineRounded(x: Int, y: Int, w: Int, h: Int, r: Int, color: Int) {
    if (w <= 1 || h <= 1) return
    val rad = r.coerceAtLeast(0).coerceAtMost(w / 2).coerceAtMost(h / 2)
    if (rad == 0) {
        // Just draw a 1px border
        fill(x, y, x + w, y + 1, color)
        fill(x, y + h - 1, x + w, y + h, color)
        fill(x, y, x + 1, y + h, color)
        fill(x + w - 1, y, x + w, y + h, color)
        return
    }

    // Top edge (between top corners)
    fill(x + rad, y, x + w - rad, y + 1, color)
    // Bottom edge
    fill(x + rad, y + h - 1, x + w - rad, y + h, color)
    // Left edge
    fill(x, y + rad, x + 1, y + h - rad, color)
    // Right edge
    fill(x + w - 1, y + rad, x + w, y + h - rad, color)

    // Corner arcs — draw only the outermost pixel of each corner slice
    for (i in 0 until rad) {
        val dy = i + 0.5f
        val dyFromCenter = rad - dy
        val r2 = rad * rad
        val dxOuter = Math.sqrt((r2 - (dyFromCenter - 1) * (dyFromCenter - 1)).toDouble()).toFloat()
        val dxInner = Math.sqrt((r2 - dyFromCenter * dyFromCenter).toDouble()).toFloat()

        val xTlOuter = (x + rad - dxOuter).toInt()
        val xTlInner = (x + rad - dxInner).toInt()
        if (xTlInner > xTlOuter) {
            fill(xTlOuter, y + i, xTlInner, y + i + 1, color)
        }
        val xTrOuter = (x + w - rad + dxOuter).toInt() + 1
        val xTrInner = (x + w - rad + dxInner).toInt()
        if (xTrOuter > xTrInner) {
            fill(xTrInner, y + i, xTrOuter, y + i + 1, color)
        }
        val xBlOuter = (x + rad - dxOuter).toInt()
        val xBlInner = (x + rad - dxInner).toInt()
        if (xBlInner > xBlOuter) {
            fill(xBlOuter, y + h - rad + i, xBlInner, y + h - rad + i + 1, color)
        }
        val xBrOuter = (x + w - rad + dxOuter).toInt() + 1
        val xBrInner = (x + w - rad + dxInner).toInt()
        if (xBrOuter > xBrInner) {
            fill(xBrInner, y + h - rad + i, xBrOuter, y + h - rad + i + 1, color)
        }
    }
}

//if this is overengineered i wouldnt even know because this is genuine unreadable slop
