package io.github.jojotastic777.hexdump.forge

import io.github.jojotastic777.hexdump.HexdumpClient
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT

object ForgeHexdumpClient {
    fun init(event: FMLClientSetupEvent) {
        HexdumpClient.init()
        LOADING_CONTEXT.registerExtensionPoint(ConfigScreenFactory::class.java) {
            ConfigScreenFactory { _, parent -> HexdumpClient.getConfigScreen(parent) }
        }
    }
}
