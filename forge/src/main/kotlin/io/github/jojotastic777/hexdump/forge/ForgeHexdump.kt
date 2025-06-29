package io.github.jojotastic777.hexdump.forge

import dev.architectury.platform.forge.EventBuses
import io.github.jojotastic777.hexdump.Hexdump
import net.minecraft.data.DataProvider
import net.minecraft.data.DataProvider.Factory
import net.minecraft.data.PackOutput
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(Hexdump.MODID)
class HexdumpForge {
    init {
        MOD_BUS.apply {
            EventBuses.registerModEventBus(Hexdump.MODID, this)
            addListener(ForgeHexdumpClient::init)
            addListener(::gatherData)
        }
        Hexdump.init()
    }

    private fun gatherData(event: GatherDataEvent) {
        event.apply {
            // TODO: add datagen providers here
        }
    }
}

fun <T : DataProvider> GatherDataEvent.addProvider(run: Boolean, factory: (PackOutput) -> T) =
    generator.addProvider(run, Factory { factory(it) })
