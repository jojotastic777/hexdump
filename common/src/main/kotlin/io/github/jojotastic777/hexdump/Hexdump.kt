package io.github.jojotastic777.hexdump

import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import io.github.jojotastic777.hexdump.config.HexdumpConfig
import io.github.jojotastic777.hexdump.networking.HexdumpNetworking
import io.github.jojotastic777.hexdump.registry.HexdumpActions

object Hexdump {
    const val MODID = "hexdump"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    @JvmStatic
    fun id(path: String) = ResourceLocation(MODID, path)

    fun init() {
        HexdumpConfig.init()
        initRegistries(
            HexdumpActions,
        )
        HexdumpNetworking.init()
    }
}
