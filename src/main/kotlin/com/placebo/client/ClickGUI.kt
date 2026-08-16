package com.placebo.client

import com.placebo.Core.modules
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class ClickGUI: Screen(Component.literal("clickgui")) {
    var mc = Minecraft.getInstance()

    fun rect(x: Int, y: Int, x1: Int, y1: Int, color: Int, graphics: GuiGraphicsExtractor) {
        graphics.fill(x, y, x1, y1, color)
    }

    fun drawRoundedRect(graphics: GuiGraphicsExtractor, x: Int, y: Int, width: Int, height: Int, radius: Int, color: Int) {
        if (radius <= 0 || width <= 0 || height <= 0) {
            rect(x, y, x + width, y + height, color, graphics)
            return
        }

        val rLimit = kotlin.math.min(radius, kotlin.math.min(width / 2, height / 2))

        val a = (color shr 24) and 0xFF
        val r = (color shr 16) and 0xFF
        val g = (color shr 8) and 0xFF
        val b = color and 0xFF

        rect(x + rLimit, y, x + width - rLimit, y + height, color, graphics) //center
        rect(x, y + rLimit, x + rLimit, y + height - rLimit, color, graphics) //thing on the left
        rect(x + width - rLimit, y + rLimit, x + width, y + height - rLimit, color, graphics) //thing on the right

        //draw the anti anlibullshit pixels
        fun drawBlendedPixel(px: Int, py: Int, factor: Float) {
            if (factor <= 0f) return
            val finalAlpha = (a * factor).toInt().coerceIn(0, 255)
            val blendedColor = (finalAlpha shl 24) or (r shl 16) or (g shl 8) or b
            rect(px, py, px + 1, py + 1, blendedColor, graphics)
        }

        val fRadius = rLimit.toFloat()


        //tl corner
        val tlCenterX = x + fRadius
        val tlCenterY = y + fRadius
        for (px in x until x + rLimit) {
            for (py in y until y + rLimit) {
                val dx = tlCenterX - (px + 0.5f)
                val dy = tlCenterY - (py + 0.5f)
                val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                val factor = (fRadius + 0.5f - dist).coerceIn(0f, 1f)
                drawBlendedPixel(px, py, factor)
            }
        }

        //tr corner
        val trCenterX = x + width - fRadius
        val trCenterY = y + fRadius
        for (px in (x + width - rLimit) until (x + width)) {
            for (py in y until y + rLimit) {
                val dx = (px + 0.5f) - trCenterX
                val dy = trCenterY - (py + 0.5f)
                val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                val factor = (fRadius + 0.5f - dist).coerceIn(0f, 1f)
                drawBlendedPixel(px, py, factor)
            }
        }

        //bl corner
        val blCenterX = x + fRadius
        val blCenterY = y + height - fRadius
        for (px in x until x + rLimit) {
            for (py in (y + height - rLimit) until (y + height)) {
                val dx = blCenterX - (px + 0.5f)
                val dy = (py + 0.5f) - blCenterY
                val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                val factor = (fRadius + 0.5f - dist).coerceIn(0f, 1f)
                drawBlendedPixel(px, py, factor)
            }
        }

        //br corner
        val brCenterX = x + width - fRadius
        val brCenterY = y + height - fRadius
        for (px in (x + width - rLimit) until (x + width)) {
            for (py in (y + height - rLimit) until (y + height)) {
                val dx = (px + 0.5f) - brCenterX
                val dy = (py + 0.5f) - brCenterY
                val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                val factor = (fRadius + 0.5f - dist).coerceIn(0f, 1f)
                drawBlendedPixel(px, py, factor)
            }
        }
    }

    fun item(graphics: GuiGraphicsExtractor, item: ItemStack, x: Int, y: Int) {
        graphics.item(item, x, y) // Can only be used while in a level
    }

    fun text(graphics: GuiGraphicsExtractor, font: Font, text: String, x: Int, y: Int, color: Int) {
        graphics.text(font, text, x, y, color)
    }

    fun outline(graphics: GuiGraphicsExtractor, x: Int, y: Int, width: Int, height: Int, color: Int) {
        graphics.outline(x, y, width, height, color)
    }

    fun centeredText(graphics: GuiGraphicsExtractor, font: Font, text: String, x: Int, y: Int, color: Int) {
        graphics.centeredText(font, text, x, y, color)
    }

    fun button(graphics: GuiGraphicsExtractor, text: String, x: Int, y: Int, width: Int, height: Int, mouseX: Int, mouseY: Int, enabled: Boolean) {
        val hovered = mouseX in x..(x + width) && mouseY in y..(y + height) // Check hover boundaries

        val color = if (hovered || enabled) 0xFF121212.toInt() else 0xFF0A0A0A.toInt()

        drawRoundedRect(graphics, x, y, width, height, 4, color)

        text(graphics, mc.font, text, x + 8, y + (height / 2) - 4, 0xFFFFFFFF.toInt())
    }

    val buttons = mutableListOf<Button>()

    data class Button(val text: String, val x: Int, val y: Int, val width: Int, val height: Int, val enabled: () -> Boolean, val onClick: () -> Unit) {
        fun contains(mouseX: Int, mouseY: Int): Boolean {
            return mouseX in x until (x + width) && mouseY in y until (y + height)
        }
    }

    fun categoryBox(graphics: GuiGraphicsExtractor, name: String, x: Int, y: Int, width: Int, height: Int) {
        drawRoundedRect(graphics, x, y, width, height, 6, 0xFF101010.toInt())

        text(graphics, mc.font, name, x + 8, y + (height / 2) - 4, 0xFFFFFFFF.toInt())
    }

    fun moduleBox(graphics: GuiGraphicsExtractor, x: Int, y: Int, width: Int, height: Int) {
        drawRoundedRect(graphics, x, y, width, height, 6, 0xFF0A0A0A.toInt())
    }

    override fun mouseClicked(click: MouseButtonEvent, doubleClick: Boolean): Boolean {
        val mouseX = click.x.toInt()
        val mouseY = click.y.toInt()
        for (button in buttons) {
            if (button.contains(mouseX, mouseY)) {
                button.onClick()
                return true
            }
        }
        return false
    }

    override fun init() {
        var x = 20
        val categories = modules.map { it.category }.distinct()
        for (category in categories) {
            val categoryModules = modules.filter { it.category == category }

            var moduleY = 40

            for (module in categoryModules) {
                buttons.add(Button(module.name, x, moduleY, 120, 20, {module.state}) {module.toggle()})
                moduleY += 25
            }
            x += 130
        }
    }

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        val categories = modules.map { it.category }.distinct()
        var x = 20

        for (category in categories) {
            val categoryModules = modules.filter { it.category == category }
            moduleBox(graphics, x, 20, 120, categoryModules.size * 25 + 10)
            categoryBox(graphics, category, x, 20, 120, 25)
                x += 130
            }
        for (button in buttons) {
            button(graphics, button.text, button.x, button.y, button.width, button.height, mouseX, mouseY, button.enabled())
        }
    }
}

