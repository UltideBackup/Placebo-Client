package com.placebo.client

import com.placebo.client.render.drawRoundedRect
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FontDescription
import net.minecraft.network.chat.Style
import net.minecraft.resources.Identifier
import org.slf4j.LoggerFactory

class ClickGuiScreen private constructor() : Screen(Component.literal("Placebo Client")) {

    private val logger = LoggerFactory.getLogger("PlaceboClient/ClickGui")

    private var previousScreen: Screen? = null
    private var s: Float = 1.0f

    /** Custom TTF font resource pointing to assets/placebo-client/font/placebo.json */
    private val customFontDescription: FontDescription by lazy {
        FontDescription.Resource(Identifier.fromNamespaceAndPath("placebo-client", "placebo"))
    }

    private fun drawCustomText(context: GuiGraphicsExtractor, text: String, x: Int, y: Int, color: Int) {
        val styledText = Component.literal(text).setStyle(Style.EMPTY.withFont(customFontDescription))
        context.text(minecraft.font, styledText, x, y, color, false)
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

    // ── Render Loop ──────────────────────────────────────────────────────

    override fun extractRenderState(context: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        val guiScale = minecraft.window.guiScale.toFloat()
        s = 2.0f / guiScale

        val sw = (width / s).toInt()
        val sh = (height / s).toInt()
        ClickGuiState.screenW = sw
        ClickGuiState.screenH = sh

        ClickGuiState.updateAnimations(0.016f)
        ClickGuiState.hoveredModule = null

        // 26.2 JOML Matrix Stack calls
        context.pose().pushMatrix()
        context.pose().scaleLocal(s, s)

        // Panels
        for (panel in ClickGuiState.panels.sortedBy { it.zIndex }) {
            renderPanel(context, panel, mouseX, mouseY)
        }

        context.pose().popMatrix()
    }

    override fun extractBackground(context: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        // Handled inside extractRenderState
    }

    // ── Panel Rendering ──────────────────────────────────────────────────

    private fun renderPanel(context: GuiGraphicsExtractor, panel: ClickGuiState.Panel, mouseX: Int, mouseY: Int) {
        val px = panel.x
        val py = panel.y
        val pw = Theme.PANEL_WIDTH.toFloat()

        val fullBody = (panel.modules.size * Theme.MODULE_ROW_H).coerceAtMost(Theme.MAX_PANEL_BODY_H).toFloat()
        val bodyH = fullBody * panel.expandAnim
        val headerH = Theme.PANEL_HEADER_H.toFloat()
        val totalH = headerH + bodyH

        // 1. Full Panel Base
        context.drawRoundedRect(
            x = px,
            y = py,
            w = pw,
            h = totalH,
            radius = Theme.PANEL_RADIUS.toFloat(),
            color = Theme.SURFACE
        )

        // 2. Header Overlay
        val isFullyCollapsed = panel.expandAnim == 0f
        context.drawRoundedRect(
            x = px,
            y = py,
            w = pw,
            h = headerH,
            radius = Theme.PANEL_RADIUS.toFloat(),
            color = Theme.HOVER,
            roundTopLeft = true,
            roundTopRight = true,
            roundBottomLeft = isFullyCollapsed,
            roundBottomRight = isFullyCollapsed
        )


        // Category Title
        val catName = panel.category
        val catX = (px + 2f).toInt()
        val catY = (py + (headerH - minecraft.font.lineHeight) / 2f + 1f).toInt()

        var tx = catX
        for (ch in catName) {
            drawCustomText(context, ch.toString(), tx, catY, Theme.TEXT)
            tx += minecraft.font.width(ch.toString()) + 1
        }


        // Expand Icon (− / +)
        val arrowX = (px + pw - 14f).toInt()
        val arrowY = (py + headerH / 2f).toInt()
        context.fill(arrowX, arrowY - 1, arrowX + 8, arrowY + 1, Theme.BORDER)
        if (panel.expandAnim < 0.5f) {
            context.fill(arrowX + 3, arrowY - 4, arrowX + 5, arrowY + 4, Theme.BORDER)
        }

        // 3. Module Rows with Scissor Clipping (Smooth animation without abrupt row deletion)
        if (bodyH > 0.5f) {
            val scissorX1 = px.toInt()
            val scissorY1 = (py + headerH).toInt()
            val scissorX2 = (px + pw).toInt()
            val scissorY2 = (py + headerH + bodyH).toInt()

            context.enableScissor(scissorX1, scissorY1, scissorX2, scissorY2)

            var rowY = py + headerH
            for (moduleRow in panel.modules) {
                // Stop rendering if row is completely below scissor boundary
                if (rowY >= py + headerH + bodyH) break

                renderModuleRow(context, moduleRow, px, rowY, pw, mouseX, mouseY)
                rowY += Theme.MODULE_ROW_H
            }

            context.disableScissor()
        }
    }

    private fun renderModuleRow(
        context: GuiGraphicsExtractor,
        row: ClickGuiState.ModuleRow,
        x: Float, y: Float, w: Float,
        mouseX: Int, mouseY: Int
    ) {
        val mx = (mouseX / s).toInt()
        val my = (mouseY / s).toInt()
        val marginX = 4f
        val paddingY = 2f

        val rowX = x + marginX
        val rowY = y + paddingY
        val rowW = w - (marginX * 2f)
        val rowH = Theme.MODULE_ROW_H.toFloat() - (paddingY * 2f)

        val xi = rowX.toInt()
        val yi = rowY.toInt()
        val wi = rowW.toInt()
        val hi = rowH.toInt()

        val isHovered = mx in xi..(xi + wi) && my in yi..(yi + hi)
        if (isHovered) ClickGuiState.hoveredModule = row

        val bg = Theme.lerpColor(Theme.SURFACE, Theme.HOVER, row.hoverAnim)
        val moduleRadius = 4f // Radius for module pill corners

        // 1. Draw Rounded Module Background
        context.drawRoundedRect(
            x = rowX,
            y = rowY,
            w = rowW,
            h = rowH,
            radius = moduleRadius,
            color = bg
        )

        // 2. Enabled State Indicator Bar (Rounded on the left)
        if (row.toggleAnim > 0f) {
            val barColor = Theme.lerpColor(Theme.BORDER, Theme.TEXT, row.toggleAnim)
            context.drawRoundedRect(
                x = rowX + 2f,
                y = rowY + 3f,
                w = 3f,
                h = rowH - 6f,
                radius = 1.5f,
                color = barColor
            )
        }

        // 3. Text Label
        val nameColor = if (row.module.state) {
            Theme.lerpColor(Theme.TEXT, 0xFFFFFFFF.toInt(), row.hoverAnim)
        } else {
            Theme.lerpColor(Theme.BORDER, Theme.TEXT, row.hoverAnim)
        }
        val nameY = yi + (hi - minecraft.font.lineHeight) / 2 + 1
        drawCustomText(context, row.module.name, xi + Theme.MODULE_PAD_X, nameY, nameColor)

    }

    // ── Input Handlers ───────────────────────────────────────────────────

    override fun mouseClicked(click: MouseButtonEvent, doubled: Boolean): Boolean {
        val mx = (click.x / s).toInt()
        val my = (click.y / s).toInt()

        if (my < 26) return true

        for (panel in ClickGuiState.panels.sortedByDescending { it.zIndex }) {
            val px = panel.x.toInt()
            val py = panel.y.toInt()
            val pw = Theme.PANEL_WIDTH
            val headerH = Theme.PANEL_HEADER_H

            if (mx in px..(px + pw) && my in py..(py + headerH)) {
                if (mx > px + pw - 20) {
                    panel.expanded = !panel.expanded
                    return true
                }
                panel.zIndex = ClickGuiState.nextZ()
                dragPanel = panel
                dragOffsetX = mx - px
                dragOffsetY = my - py
                return true
            }

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

    override fun shouldCloseOnEsc(): Boolean = true
    override fun isPauseScreen(): Boolean = false

    private var dragPanel: ClickGuiState.Panel? = null
    private var dragOffsetX: Int = 0
    private var dragOffsetY: Int = 0
}