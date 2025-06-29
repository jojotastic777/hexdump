package io.github.jojotastic777.hexdump

import io.github.jojotastic777.hexdump.config.HexdumpConfig
import io.github.jojotastic777.hexdump.networking.HexdumpNetworking
import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.api.utils.isOfTag
import at.petrak.hexcasting.xplat.IXplatAbstractions
import com.google.gson.Gson
import dev.architectury.event.events.common.CommandRegistrationEvent
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import net.minecraft.commands.Commands.literal
import net.minecraft.network.chat.Component

object Hexdump {
    const val MODID = "hexdump"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    @JvmStatic
    fun id(path: String) = ResourceLocation(MODID, path)

    fun init() {
        HexdumpConfig.init()
        HexdumpNetworking.init()

        val actionsRegistry = IXplatAbstractions.INSTANCE.actionRegistry
        val gson = Gson()

        CommandRegistrationEvent.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(literal("hexdump")
                .executes { context ->
                    val patterns = HashMap<String, Any>()

                    for (id in actionsRegistry.registryKeySet()) {
                        val entry = actionsRegistry.get(id)
                        if (entry == null) { continue }

                        val obj = object {
                            val id = id.location().toString()
                            val startDir = entry.prototype.startDir
                            val angles = entry.prototype.anglesSignature()
                            val isPerWorld = isOfTag(actionsRegistry, id, HexTags.Actions.PER_WORLD_PATTERN)
                        }

                        patterns[id.location().toString()] = obj
                    }

                    val file = java.io.File("patterns.json")
                    file.writeText(gson.toJson(patterns))

                    context.source.sendSuccess({-> Component.literal("Dumped actions registry to patterns.json")}, false)

                    1
                })
        }
    }
}
