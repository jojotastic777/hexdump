@file:JvmName("HexdumpAbstractions")

package io.github.jojotastic777.hexdump

import dev.architectury.injectables.annotations.ExpectPlatform
import io.github.jojotastic777.hexdump.registry.HexdumpRegistrar

fun initRegistries(vararg registries: HexdumpRegistrar<*>) {
    for (registry in registries) {
        initRegistry(registry)
    }
}

@ExpectPlatform
fun <T : Any> initRegistry(registrar: HexdumpRegistrar<T>) {
    throw AssertionError()
}
