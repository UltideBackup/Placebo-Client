package com.placebo.client

object Theme {
    // ── Palette ──────────────────────────────────────────────────────────
    const val BACKDROP: Int = 0xD0000000.toInt()
    const val SURFACE:  Int = 0xFF141414.toInt()
    const val HOVER:    Int = 0xFF222222.toInt()
    const val BORDER:   Int = 0xFF383838.toInt()
    const val TEXT:     Int = 0xFFEAEAEA.toInt()

    // ── Layout Metrics ──────────────────────────────────────────────────
    const val PANEL_WIDTH:      Int = 178
    const val PANEL_HEADER_H:   Int = 24
    const val MODULE_ROW_H:     Int = 17
    const val MODULE_PAD_X:     Int = 11
    const val PANEL_RADIUS:     Int = 6
    const val MAX_PANEL_BODY_H: Int = 280

    // ── Color Utilities ──────────────────────────────────────────────────
    fun withAlpha(color: Int, alphaFloat: Float): Int {
        val a = (alphaFloat.coerceIn(0f, 1f) * 255).toInt()
        return (a shl 24) or (color and 0x00FFFFFF)
    }

    fun lerpColor(from: Int, to: Int, progress: Float): Int {
        val t = progress.coerceIn(0f, 1f)
        val a = lerp((from ushr 24 and 0xFF).toFloat(), (to ushr 24 and 0xFF).toFloat(), t).toInt()
        val r = lerp((from ushr 16 and 0xFF).toFloat(), (to ushr 16 and 0xFF).toFloat(), t).toInt()
        val g = lerp((from ushr 8 and 0xFF).toFloat(), (to ushr 8 and 0xFF).toFloat(), t).toInt()
        val b = lerp((from and 0xFF).toFloat(), (to and 0xFF).toFloat(), t).toInt()
        return (a shl 24) or (r shl 16) or (g shl 8) or b
    }

    private fun lerp(start: Float, stop: Float, amount: Float): Float = start + (stop - start) * amount
}