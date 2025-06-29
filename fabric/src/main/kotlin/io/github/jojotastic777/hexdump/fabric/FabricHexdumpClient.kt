package io.github.jojotastic777.hexdump.fabric

import io.github.jojotastic777.hexdump.HexdumpClient
import net.fabricmc.api.ClientModInitializer

object FabricHexdumpClient : ClientModInitializer {
    override fun onInitializeClient() {
        HexdumpClient.init()
    }
}
