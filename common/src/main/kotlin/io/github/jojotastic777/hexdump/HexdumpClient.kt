package io.github.jojotastic777.hexdump

import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.api.utils.isOfTag
import at.petrak.hexcasting.xplat.IXplatAbstractions
import com.google.gson.Gson
import dev.architectury.event.events.client.ClientCommandRegistrationEvent
import dev.architectury.event.events.client.ClientCommandRegistrationEvent.literal
import io.github.jojotastic777.hexdump.config.HexdumpConfig
import io.github.jojotastic777.hexdump.config.HexdumpConfig.GlobalConfig
import me.shedaniel.autoconfig.AutoConfig
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

object HexdumpClient {
    fun init() {
        HexdumpConfig.initClient()

        val actionsRegistry = IXplatAbstractions.INSTANCE.actionRegistry
        val gson = Gson()

        ClientCommandRegistrationEvent.EVENT.register { dispatcher, _ ->
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

                    context.source.`arch$sendSuccess`({-> Component.literal("Dumped actions registry to patterns.json")}, false)

                    1
                })
        }

    }

    fun getConfigScreen(parent: Screen): Screen {
        return AutoConfig.getConfigScreen(GlobalConfig::class.java, parent).get()
    }
}
