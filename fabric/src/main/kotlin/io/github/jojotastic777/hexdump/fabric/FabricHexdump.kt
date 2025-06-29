package io.github.jojotastic777.hexdump.fabric

import io.github.jojotastic777.hexdump.Hexdump
import net.fabricmc.api.ModInitializer

object FabricHexdump : ModInitializer {
    override fun onInitialize() {
        Hexdump.init()
    }
}
