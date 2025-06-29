@file:JvmName("HexdumpAbstractionsImpl")

package io.github.jojotastic777.hexdump.forge

import io.github.jojotastic777.hexdump.registry.HexdumpRegistrar
import net.minecraftforge.registries.RegisterEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS

fun <T : Any> initRegistry(registrar: HexdumpRegistrar<T>) {
    MOD_BUS.addListener { event: RegisterEvent ->
        event.register(registrar.registryKey) { helper ->
            registrar.init(helper::register)
        }
    }
}
