package com.placebo.Modules.World

import com.placebo.Core.Classes.Module
import net.minecraft.client.Minecraft
import net.minecraft.world.inventory.ContainerInput

class ChestStealer : Module("Chest Stealer") {

    override var category = "World"
    override var state = false

    private val mc = Minecraft.getInstance()

    override fun Tick() {
        val gamemode = mc.gameMode?: return
        val player = mc.player ?: return
        val container = player.containerMenu

        var slots = container.slots

            for (slot in slots){
                if (slot.container != player.inventory && state){
                if (!slot.item.isEmpty){
gamemode.handleContainerInput(
    container.containerId,
    slot.index,
    0,
    ContainerInput.QUICK_MOVE,
    player
)
                    break//lowkey forgot to add this shit
                }
            }
        }
            }
        }
