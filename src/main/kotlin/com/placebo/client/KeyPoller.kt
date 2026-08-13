package com.placebo.client

// ═══════════════════════════════════════════════════════════════════════════
// the clickgui + decoy modules were made by ai i cannot be bothered to fucking make this shit
// ═══════════════════════════════════════════════════════════════════════════
//
// KeyPoller.kt — polls RIGHT-SHIFT every MC tick, toggles the ClickGUI.
// Works everywhere: in-game, main menu, pause screen, etc.
// ═══════════════════════════════════════════════════════════════════════════

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.Minecraft
import org.lwjgl.glfw.GLFW
import org.slf4j.LoggerFactory

object KeyPoller {

    private val LOGGER = LoggerFactory.getLogger("PlaceboClient/KeyPoller")
    private const val OPEN_GUI_KEY: Int = GLFW.GLFW_KEY_RIGHT_SHIFT
    private var wasKeyDown: Boolean = false

    @JvmStatic
    fun tick() {
        val mc = Minecraft.getInstance()
        val isKeyDown = InputConstants.isKeyDown(mc.window, OPEN_GUI_KEY)

        // Edge detection: key was down, now up = "press" event
        if (wasKeyDown && !isKeyDown) {
            val current = mc.gui.screen()
            if (current is ClickGuiScreen) {
                // Already open → close it (toggle behavior)
                current.onClose()
            } else {
                // Not open → open it (works from any screen: game, title, pause, etc.)
                LOGGER.info("[Placebo] Opening ClickGUI")
                ClickGuiScreen.open(mc)
            }
        }

        wasKeyDown = isKeyDown
    }
}
