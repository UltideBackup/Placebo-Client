package com.placebo.client

// ═══════════════════════════════════════════════════════════════════════════
// the clickgui + decoy modules were made by ai i cannot be bothered to fucking make this shit
// ═══════════════════════════════════════════════════════════════════════════

import com.placebo.Useless.modules
import com.placebo.Useless.uselessclasses.coide
import java.io.File
import java.util.Properties

object ClickGuiState {

    data class Panel(
        val category: String,
        var x: Float,
        var y: Float,
        var expanded: Boolean = true,
        var expandAnim: Float = 1f,
        var zIndex: Int = 0,
        var modules: List<ModuleRow> = emptyList()
    )

    data class ModuleRow(
        val module: coide,
        var hoverAnim: Float = 0f,
        var toggleAnim: Float = if (module.state) 1f else 0f
    )

    val panels: MutableList<Panel> = mutableListOf()

    /** Screen dimensions in our scaled coord system (updated each frame). */
    var screenW: Int = 1920
    var screenH: Int = 1080

    var hoveredModule: ModuleRow? = null

    private var zCounter: Int = 0
    fun nextZ(): Int = ++zCounter

    // ── Init ──────────────────────────────────────────────────────────────

    fun initialize() {
        if (panels.isNotEmpty()) return

        val grouped = modules.groupBy { categorize(it) }
        val categories = grouped.keys.sortedWith(compareBy(
            { if (it == "Misc") 1 else 0 },
            { it }
        ))

        val colCount = 4
        val margin = 20
        val gap = 10
        val rowStep = 200

        categories.forEachIndexed { i, cat ->
            val col = i % colCount
            val row = i / colCount
            panels.add(Panel(
                category = cat,
                x = (margin + col * (Theme.PANEL_WIDTH + gap)).toFloat(),
                y = (40 + row * rowStep).toFloat(),
                expanded = true,
                expandAnim = 1f,
                zIndex = i,
                modules = grouped[cat]!!.map { ModuleRow(it) }
            ))
        }
        zCounter = panels.size
        loadFromConfig()
    }

    // ── Categorize ────────────────────────────────────────────────────────

    private fun categorize(m: coide): String {
        val n = m.name.lowercase()
        return when {
            n.contains("kill") || n.contains("aura") || n.contains("hit") ||
            n.contains("attack") || n.contains("crit") || n.contains("aimbot") ||
            n.contains("reach") || n.contains("velocity") || n.contains("antikb") ||
            n.contains("antiknockback") -> "Combat"

            n.contains("speed") || n.contains("fast") || n.contains("fly") ||
            n.contains("jump") || n.contains("sprint") || n.contains("step") ||
            n.contains("noclip") || n.contains("phase") || n.contains("jesus") ||
            n.contains("spider") || n.contains("glide") || n.contains("parkour") ||
            n.contains("safewalk") || n.contains("airjump") || n.contains("autowalk") -> "Movement"

            n.contains("chest") || n.contains("steel") || n.contains("inv") ||
            n.contains("auto") || n.contains("totem") || n.contains("gapple") ||
            n.contains("armor") || n.contains("refill") || n.contains("eat") ||
            n.contains("pot") || n.contains("fish") || n.contains("farm") ||
            n.contains("mine") -> "Player"

            n.contains("chat") || n.contains("name") || n.contains("nick") ||
            n.contains("antiaim") || n.contains("spin") || n.contains("autoafk") ||
            n.contains("autogg") || n.contains("autoqueue") || n.contains("nameprotect") ||
            n.contains("autoclicker") || n.contains("cps") -> "Client"

            n.contains("render") || n.contains("esp") || n.contains("tracer") ||
            n.contains("hud") || n.contains("nametag") || n.contains("chams") ||
            n.contains("outline") || n.contains("xray") || n.contains("bright") ||
            n.contains("nightvision") || n.contains("camera") || n.contains("crosshair") ||
            n.contains("fullbright") || n.contains("zoom") || n.contains("shader") -> "Render"

            n.contains("world") || n.contains("nuker") || n.contains("scaffold") ||
            n.contains("bridge") || n.contains("bidge") || n.contains("breaker") ||
            n.contains("fastplace") || n.contains("fastbreak") || n.contains("waypoints") -> "World"

            else -> "Misc"
        }
    }

    // ── Animation ────────────────────────────────────────────────────────

    fun updateAnimations(dt: Float) {
        val k = (dt * 60f * 0.18f).coerceIn(0f, 1f)
        for (panel in panels) {
            val target = if (panel.expanded) 1f else 0f
            panel.expandAnim += (target - panel.expandAnim) * k
            for (row in panel.modules) {
                val hTarget = if (row === hoveredModule) 1f else 0f
                row.hoverAnim += (hTarget - row.hoverAnim) * k
                val tTarget = if (row.module.state) 1f else 0f
                row.toggleAnim += (tTarget - row.toggleAnim) * k
            }
        }
    }

    // ── Persistence ──────────────────────────────────────────────────────

    private fun configFile(): File {
        val dir = File(System.getProperty("user.home"), ".placebo")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "clickgui.properties")
    }

    private fun loadFromConfig() {
        val props = Properties()
        val file = configFile()
        if (!file.exists()) return
        runCatching {
            file.inputStream().use { props.load(it) }
            for (panel in panels) {
                props.getProperty("panel.${panel.category}.x")?.toFloatOrNull()?.let { panel.x = it }
                props.getProperty("panel.${panel.category}.y")?.toFloatOrNull()?.let { panel.y = it }
                props.getProperty("panel.${panel.category}.expanded")?.toBoolean()?.let {
                    panel.expanded = it
                    panel.expandAnim = if (it) 1f else 0f
                }
            }
        }
    }

    fun saveToConfig() {
        val props = Properties()
        for (panel in panels) {
            props.setProperty("panel.${panel.category}.x", panel.x.toString())
            props.setProperty("panel.${panel.category}.y", panel.y.toString())
            props.setProperty("panel.${panel.category}.expanded", panel.expanded.toString())
        }
        runCatching { configFile().outputStream().use { props.store(it, "Placebo ClickGUI") } }
    }
}
