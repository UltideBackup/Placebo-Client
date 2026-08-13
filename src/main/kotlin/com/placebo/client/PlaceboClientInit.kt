package com.placebo.client

// ═══════════════════════════════════════════════════════════════════════════
// the clickgui + decoy modules were made by ai i cannot be bothered to fucking make this shit
// ═══════════════════════════════════════════════════════════════════════════
//
// PlaceboClientInit.kt
// --------------------
// Client-side entrypoint. Just logs that the client is loaded.
//
// Key polling for the ClickGUI is handled by KeyPoller.tick(), which is called
// from ExampleMixin.java every Minecraft tick — no Fabric key-mapping API
// dependency needed.
// ═══════════════════════════════════════════════════════════════════════════

import net.fabricmc.api.ClientModInitializer
import org.slf4j.LoggerFactory

class PlaceboClientInit : ClientModInitializer {

    private val logger = LoggerFactory.getLogger("PlaceboClient/Init")
    override fun onInitializeClient() {
        //why print anything we arent debugging?
    }
}
