package com.placebo.client

// ═══════════════════════════════════════════════════════════════════════════
// the clickgui + decoy modules were made by ai i cannot be bothered to fucking make this shit
// ═══════════════════════════════════════════════════════════════════════════
//
// ClickGuiScreen.kt — native MC 26.2 Screen, rounded monochrome panels.
//
// Scaling: pushes a pose scale of 2.0/guiScale so the UI always renders at
// "GUI scale 2" equivalent regardless of the user's GUI scale setting.
//
// Font: loads a custom Carlito TTF via MC's font system (assets/placebo-client/font/).
// Falls back to the default MC font if the custom font fails to load.
// ═══════════════════════════════════════════════════════════════════════════

import com.placebo.Core.modules
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import org.slf4j.LoggerFactory

class ClickGuiScreen private constructor() : Screen(Component.literal("Placebo Client")) {

    private val logger = LoggerFactory.getLogger("PlaceboClient/ClickGui")

    /** Previous screen — restored when the ClickGUI closes. */
    private var previousScreen: Screen? = null

    /** Pose scale factor — converts GUI-scaled coords to our fixed-scale coords. */
    private var s: Float = 1.0f

    /**
     * Custom font loaded from assets/placebo-client/font/placebo.json.
     * Uses reflection to access minecraft.fontManager.getFont(id) so the code
     * compiles even if the FontManager API isn't directly accessible.
     * Falls back to minecraft.font on any error.
     */
    private val uiFont: Font by lazy {
        try {
            val id = Identifier.fromNamespaceAndPath("placebo-client", "placebo")
            val mc = minecraft

            // Try to access minecraft.fontManager via reflection
            val fontManagerField = mc.javaClass.fields.firstOrNull { it.name == "fontManager" }
                ?: mc.javaClass.superclass?.fields?.firstOrNull { it.name == "fontManager" }

            if (fontManagerField != null) {
                val fm = fontManagerField.get(mc)
                // Try getFont(Identifier) method — accept any single-arg overload
                // named "getFont" and try invoking it with our Identifier.
                val getFontMethods = fm.javaClass.methods.filter { it.name == "getFont" && it.parameterCount == 1 }
                for (m in getFontMethods) {
                    try {
                        val resolved = m.invoke(fm, id) as? Font
                        if (resolved != null) {
                            logger.info("[Placebo] Loaded custom Carlito font: $id")
                            return@lazy resolved
                        }
                    } catch (_: Throwable) {
                        // try the next overload
                    }
                }
            }

            logger.warn("[Placebo] Custom font not accessible, falling back to default")
            mc.font
        } catch (e: Throwable) {
            logger.warn("[Placebo] Failed to load custom font, falling back to default", e)
            minecraft.font
        }
    }

    companion object {
        fun open(mc: Minecraft) {
            val prev = mc.gui.screen()
            val screen = ClickGuiScreen()
            screen.previousScreen = prev
            mc.gui.setScreen(screen)
        }
    }

    override fun init() {
        ClickGuiState.initialize()
    }

    override fun onClose() {
        ClickGuiState.saveToConfig()
        minecraft.gui.setScreen(previousScreen)
    }

    // ── Render ───────────────────────────────────────────────────────────

    override fun extractRenderState(context: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        val guiScale = minecraft.window.guiScale.toFloat()
        s = 2.0f / guiScale

        val sw = (width / s).toInt()
        val sh = (height / s).toInt()
        ClickGuiState.screenW = sw
        ClickGuiState.screenH = sh

        ClickGuiState.updateAnimations(0.016f)
        ClickGuiState.hoveredModule = null

        // Push pose scale
        context.pose().pushMatrix()
        context.pose().scale(s, s)

        // ── Backdrop (rounded vignette) ───────────────────────────────────
        context.fill(0, 0, sw, sh, Theme.BACKDROP)

        // ── Header bar (subtle, optional branding) ────────────────────────
        renderHeader(context, sw)

        // ── Panels ────────────────────────────────────────────────────────
        for (panel in ClickGuiState.panels.sortedBy { it.zIndex }) {
            renderPanel(context, panel, mouseX, mouseY)
        }

        context.pose().popMatrix()
    }

    override fun extractBackground(context: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        // empty — we draw our own backdrop
    }

    // ── Header ───────────────────────────────────────────────────────────

    private fun renderHeader(context: GuiGraphicsExtractor, sw: Int) {
        val headerH = 26
        // Top bar background
        context.fill(0, 0, sw, headerH, Theme.INK_100)
        // Bottom border line
        context.fill(0, headerH, sw, headerH + 1, Theme.INK_300)

        // "PLACEBO" wordmark on the left
        val brand = "PLACEBO"
        val brandX = 16
        val brandY = (headerH - uiFont.lineHeight) / 2
        // Draw with a slight letter-spacing feel by drawing each char individually
        var cx = brandX
        for (ch in brand) {
            context.text(uiFont, ch.toString(), cx, brandY, Theme.INK_1000, true)
            cx += uiFont.width(ch.toString()) + 1
        }

        // Subtitle "client v1.0.0" in muted color
        val sub = "client v1.0.0"
        context.text(uiFont, sub, cx + 6, brandY, Theme.INK_600, false)

        // Right side: module count
        val total = modules.size
        val enabled = modules.count { it.state }
        val stats = "$enabled / $total"
        val statsW = uiFont.width(stats)
        context.text(uiFont, stats, sw - statsW - 16, brandY, Theme.INK_700, false)
    }

    // ── Panel ────────────────────────────────────────────────────────────

    private fun renderPanel(context: GuiGraphicsExtractor, panel: ClickGuiState.Panel, mouseX: Int, mouseY: Int) {
        val px = panel.x.toInt()
        val py = panel.y.toInt()
        val pw = Theme.PANEL_WIDTH

        val fullBody = (panel.modules.size * Theme.MODULE_ROW_H).coerceAtMost(Theme.MAX_PANEL_BODY_H)
        val bodyH = (fullBody * panel.expandAnim).toInt()
        val headerH = Theme.PANEL_HEADER_H
        val totalH = headerH + bodyH

        // ── Drop shadow (subtle, offset down-right) ───────────────────────
        context.fillRounded(px + 2, py + 3, pw, totalH, Theme.PANEL_RADIUS, Theme.INK_0 and 0x66000000.toInt())

        // ── Panel background (rounded) ────────────────────────────────────
        context.fillRounded(px, py, pw, totalH, Theme.PANEL_RADIUS, Theme.INK_100)

        // ── Header background (rounded top only, slightly lighter) ────────
        context.fillRoundedTop(px, py, pw, headerH, Theme.PANEL_RADIUS, Theme.INK_150)

        // Header bottom border (subtle separator)
        context.fill(px + 1, py + headerH, px + pw - 1, py + headerH + 1, Theme.INK_250)

        // ── Category dot indicator ────────────────────────────────────────
        val dotR = 3
        val dotX = px + 11
        val dotY = py + headerH / 2 - dotR
        val dotColor = if (panel.expandAnim > 0.5f) Theme.INK_1000 else Theme.INK_500
        context.fill(dotX, dotY, dotX + dotR * 2, dotY + dotR * 2, dotColor)

        // ── Category name (uppercase, tracked) ────────────────────────────
        val catName = panel.category.uppercase()
        val catX = px + 22
        val catY = py + (headerH - uiFont.lineHeight) / 2 + 1
        // Draw with letter spacing for a cleaner look
        var tx = catX
        for (ch in catName) {
            context.text(uiFont, ch.toString(), tx, catY, Theme.INK_900, false)
            tx += uiFont.width(ch.toString()) + 1
        }

        // ── Module count badge on the right ───────────────────────────────
        val countText = panel.modules.size.toString()
        val countW = uiFont.width(countText) + 8
        val countX = px + pw - countW - 22
        val countY = py + (headerH - 11) / 2
        // Skip the badge background — keep it minimal, just text
        context.text(uiFont, countText, countX + 4, catY, Theme.INK_550, false)

        // ── Expand toggle (− or +) on the far right ───────────────────────
        val arrowX = px + pw - 14
        val arrowY = py + headerH / 2
        val arrowColor = Theme.INK_650
        // Always draw horizontal bar
        context.fill(arrowX, arrowY - 1, arrowX + 8, arrowY + 1, arrowColor)
        // If collapsed, also draw vertical bar (forming a "+")
        if (panel.expandAnim < 0.5f) {
            context.fill(arrowX + 3, arrowY - 4, arrowX + 5, arrowY + 4, arrowColor)
        }

        // ── Module rows ───────────────────────────────────────────────────
        if (bodyH > 0) {
            val maxRows = bodyH / Theme.MODULE_ROW_H
            var rowY = py + headerH
            for (i in panel.modules.indices) {
                if (i >= maxRows) break
                renderModuleRow(context, panel.modules[i], px, rowY, pw, mouseX, mouseY)
                rowY += Theme.MODULE_ROW_H
            }
        }

        // ── Panel outline (1px rounded border) ────────────────────────────
        context.outlineRounded(px, py, pw, totalH, Theme.PANEL_RADIUS, Theme.INK_300)
    }

    private fun renderModuleRow(
        context: GuiGraphicsExtractor,
        row: ClickGuiState.ModuleRow,
        x: Int, y: Int, w: Int,
        mouseX: Int, mouseY: Int
    ) {
        val mx = (mouseX / s).toInt()
        val my = (mouseY / s).toInt()
        val isHovered = mx in x..(x + w) && my in y..(y + Theme.MODULE_ROW_H)
        if (isHovered) ClickGuiState.hoveredModule = row

        // Row background — smooth hover lerp
        val bg = Theme.lerpColor(Theme.INK_100, Theme.INK_150, row.hoverAnim)
        context.fill(x + 1, y, x + w - 1, y + Theme.MODULE_ROW_H, bg)

        // Left edge toggle bar (animates in)
        if (row.toggleAnim > 0f) {
            val barColor = Theme.lerpColor(Theme.INK_300, Theme.INK_1000, row.toggleAnim)
            // Rounded left edge effect: draw a 2px wide bar
            context.fill(x + 1, y + 2, x + 3, y + Theme.MODULE_ROW_H - 2, barColor)
        }

        // Module name — color shifts on hover + toggle state
        val nameColor = if (row.module.state) {
            Theme.lerpColor(Theme.INK_900, Theme.INK_1000, row.hoverAnim)
        } else {
            Theme.lerpColor(Theme.INK_600, Theme.INK_850, row.hoverAnim)
        }
        val nameY = y + (Theme.MODULE_ROW_H - uiFont.lineHeight) / 2 + 1
        context.text(uiFont, row.module.name, x + Theme.MODULE_PAD_X, nameY, nameColor, false)

        // Toggle dot on the right
        val dotSize = 5
        val dotX = x + w - Theme.MODULE_PAD_X - dotSize
        val dotY = y + (Theme.MODULE_ROW_H - dotSize) / 2

        // Glow halo when enabled (subtle)
        if (row.toggleAnim > 0.3f) {
            val glowAlpha = ((row.toggleAnim - 0.3f) / 0.7f) * 0.35f
            val glowColor = Theme.withAlpha(Theme.INK_1000, glowAlpha)
            // Draw a slightly larger faded rect for the glow
            context.fill(dotX - 2, dotY - 2, dotX + dotSize + 2, dotY + dotSize + 2, glowColor)
        }

        val dotColor = Theme.lerpColor(Theme.INK_350, Theme.INK_1000, row.toggleAnim)
        context.fill(dotX, dotY, dotX + dotSize, dotY + dotSize, dotColor)
    }

    // ── Mouse ────────────────────────────────────────────────────────────

    override fun mouseClicked(click: MouseButtonEvent, doubled: Boolean): Boolean {
        val mx = (click.x / s).toInt()
        val my = (click.y / s).toInt()

        // Header bar click area — ignore (no behavior)
        if (my < 26) return true

        for (panel in ClickGuiState.panels.sortedByDescending { it.zIndex }) {
            val px = panel.x.toInt()
            val py = panel.y.toInt()
            val pw = Theme.PANEL_WIDTH
            val headerH = Theme.PANEL_HEADER_H

            // Click on header → drag or toggle expand
            if (mx in px..(px + pw) && my in py..(py + headerH)) {
                // Expand arrow click area
                if (mx > px + pw - 20) {
                    panel.expanded = !panel.expanded
                    return true
                }
                // Start dragging
                panel.zIndex = ClickGuiState.nextZ()
                dragPanel = panel
                dragOffsetX = mx - px
                dragOffsetY = my - py
                return true
            }

            // Click on a module row → toggle
            if (panel.expanded) {
                val bodyY = py + headerH
                val fullBody = (panel.modules.size * Theme.MODULE_ROW_H).coerceAtMost(Theme.MAX_PANEL_BODY_H)
                val bodyH = (fullBody * panel.expandAnim).toInt()
                if (mx in px..(px + pw) && my in bodyY..(bodyY + bodyH)) {
                    val rowIdx = (my - bodyY) / Theme.MODULE_ROW_H
                    if (rowIdx in panel.modules.indices) {
                        panel.modules[rowIdx].module.state = !panel.modules[rowIdx].module.state
                        return true
                    }
                }
            }
        }
        return true
    }

    override fun mouseDragged(click: MouseButtonEvent, offsetX: Double, offsetY: Double): Boolean {
        val panel = dragPanel ?: return true
        val mx = (click.x / s).toInt()
        val my = (click.y / s).toInt()

        panel.x = (mx - dragOffsetX).toFloat()
            .coerceIn(0f, (ClickGuiState.screenW - Theme.PANEL_WIDTH).toFloat())
        panel.y = (my - dragOffsetY).toFloat()
            .coerceIn(26f, (ClickGuiState.screenH - Theme.PANEL_HEADER_H).toFloat())
        return true
    }

    override fun mouseReleased(click: MouseButtonEvent): Boolean {
        dragPanel = null
        return true
    }

    // ── Keyboard ─────────────────────────────────────────────────────────

    override fun shouldCloseOnEsc(): Boolean = true
    override fun isPauseScreen(): Boolean = false

    private var dragPanel: ClickGuiState.Panel? = null
    private var dragOffsetX: Int = 0
    private var dragOffsetY: Int = 0
}
