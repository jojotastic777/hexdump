@file:JvmName("HexdumpAbstractionsImpl")

package io.github.jojotastic777.hexdump.fabric

import io.github.jojotastic777.hexdump.registry.HexdumpRegistrar
import net.minecraft.core.Registry

fun <T : Any> initRegistry(registrar: HexdumpRegistrar<T>) {
    val registry = registrar.registry
    registrar.init { id, value -> Registry.register(registry, id, value) }
}
