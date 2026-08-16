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
        if (wasKeyDown && !isKeyDown) {
            val current = mc.gui.screen()
            if (current is ClickGUI) {
                current.onClose()
            } else {
                Minecraft.getInstance().gui.setScreen(ClickGUI())
            }
        }

        wasKeyDown = isKeyDown
    }
}
