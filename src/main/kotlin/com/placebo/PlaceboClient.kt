package com.placebo

import com.placebo.Core.Listener
import com.placebo.Core.modules
import com.placebo.Core.registerListener
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier
import org.slf4j.LoggerFactory

object PlaceboClient : ModInitializer {
    const val MOD_ID: String = "placebo-client"

    private val LOGGER = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        for (module in modules){
            if (module is Listener){
                registerListener(module as Listener)
            }
        }
    }

    fun id(path: String): Identifier
        = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
