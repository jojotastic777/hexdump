package io.github.jojotastic777.hexdump.networking

import dev.architectury.networking.NetworkChannel
import io.github.jojotastic777.hexdump.Hexdump
import io.github.jojotastic777.hexdump.networking.msg.HexdumpMessageCompanion

object HexdumpNetworking {
    val CHANNEL: NetworkChannel = NetworkChannel.create(Hexdump.id("networking_channel"))

    fun init() {
        for (subclass in HexdumpMessageCompanion::class.sealedSubclasses) {
            subclass.objectInstance?.register(CHANNEL)
        }
    }
}
