package io.github.jojotastic777.hexdump

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
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
import net.minecraft.commands.Commands
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component

object HexdumpClient {
    fun dumpPatterns(registry: Registry<ActionRegistryEntry>, includePerWorldPatterns: Boolean) {
        val gson = Gson()

        val patterns = HashMap<String, Any>()

        for (id in registry.registryKeySet()) {
            val action = registry.get(id)
            if (action == null) {  continue }

            val isPerWorld = isOfTag(registry, id, HexTags.Actions.PER_WORLD_PATTERN)

            if (!includePerWorldPatterns && isPerWorld) { continue }

            val pat = object {
                val id = id.location().toString()
                val name = Component.translatable("hexcasting.action.${id.location()}").string
                val startDir = action.prototype.startDir
                val angles = action.prototype.anglesSignature()
                val isPerWorld = isPerWorld
            }

            patterns[id.location().toString()] = pat
        }

        val file = java.io.File("patterns.json")
        file.writeText(gson.toJson(patterns))
    }

    fun init() {
        HexdumpConfig.initClient()

        val actionsRegistry = IXplatAbstractions.INSTANCE.actionRegistry

        ClientCommandRegistrationEvent.EVENT.register { dispatcher, _ ->
            dispatcher.register(literal("hexdump")
                .executes { context ->
                    dumpPatterns(actionsRegistry, false)

                    context.source.`arch$sendSuccess`({-> Component.literal("Dumped actions registry to patterns.json")}, false)

                    1
                }
                .then(literal("with-per-world")
                    .requires { source -> source.hasPermission(Commands.LEVEL_ADMINS) }
                    .executes { context ->
                        dumpPatterns(actionsRegistry, true)

                        context.source.`arch$sendSuccess`({-> Component.literal("Dumped actions registry to patterns.json")}, false)

                        1
                    }))
        }

    }

    fun getConfigScreen(parent: Screen): Screen {
        return AutoConfig.getConfigScreen(GlobalConfig::class.java, parent).get()
    }
}
