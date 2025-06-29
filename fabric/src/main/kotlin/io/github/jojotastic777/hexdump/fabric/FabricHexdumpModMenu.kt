package io.github.jojotastic777.hexdump.fabric

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import io.github.jojotastic777.hexdump.HexdumpClient

object FabricHexdumpModMenu : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory(HexdumpClient::getConfigScreen)
}
