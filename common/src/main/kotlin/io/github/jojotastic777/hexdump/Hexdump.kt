package io.github.jojotastic777.hexdump

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.api.utils.isOfTag
import at.petrak.hexcasting.common.casting.PatternRegistryManifest
import at.petrak.hexcasting.xplat.IXplatAbstractions
import com.mojang.brigadier.context.CommandContext
import dev.architectury.event.events.common.CommandRegistrationEvent
import io.github.jojotastic777.hexdump.config.HexdumpConfig
import io.github.jojotastic777.hexdump.networking.HexdumpNetworking
import io.github.jojotastic777.hexdump.networking.msg.PatternDumpS2C
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.Commands.literal
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Hexdump {
    const val MODID = "hexdump"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    @JvmStatic
    fun id(path: String) = ResourceLocation(MODID, path)

    fun dumpPatterns(registry: Registry<ActionRegistryEntry>, ctx: CommandContext<CommandSourceStack>, includePerWorldPatterns: Boolean) {
        val player = ctx.source.player
        if (player == null) { return }

        val patterns = HashMap<String, PatternDump.DumpEntry>()

        for (id in registry.registryKeySet()) {
            val action = registry.get(id)
            if (action == null) continue

            val isPerWorld = isOfTag(registry, id, HexTags.Actions.PER_WORLD_PATTERN)

            if (!includePerWorldPatterns && isPerWorld) { continue }

            val pat = if (isPerWorld) {
                val foundPat = PatternRegistryManifest.getCanonicalStrokesPerWorld(id, ctx.source.server.overworld())
                if (foundPat == null) continue

                PatternDump.DumpEntry(
                    id = id.location().toString(),
                    name = Component.translatable("hexcasting.action.${id.location()}").string,
                    startDir = foundPat.startDir.toString(),
                    angles = foundPat.anglesSignature(),
                    isPerWorld = true
                )
            } else {
                PatternDump.DumpEntry(
                    id = id.location().toString(),
                    name = Component.translatable("hexcasting.action.${id.location()}").string,
                    startDir = action.prototype.startDir.toString(),
                    angles = action.prototype.anglesSignature(),
                    isPerWorld = false
                )
            }

            patterns[id.location().toString()] = pat
        }

        PatternDumpS2C(patterns).sendToPlayer(player)
    }

    fun init() {
        HexdumpConfig.init()
        HexdumpNetworking.init()

        val actionsRegistry = IXplatAbstractions.INSTANCE.actionRegistry

        CommandRegistrationEvent.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(literal("hexdump")
                .executes { context ->
                    dumpPatterns(actionsRegistry, context, false)

                    context.source.sendSuccess({-> Component.literal("Success.")}, false)

                    1
                }
                .then(literal("with-world-patterns")
                    .requires { source -> source.hasPermission(Commands.LEVEL_ADMINS) }
                    .executes { context ->
                        dumpPatterns(actionsRegistry, context, true)

                        context.source.sendSuccess({-> Component.literal("Success.")}, false)

                        1
                    }
                )
            )
        }
    }
}
