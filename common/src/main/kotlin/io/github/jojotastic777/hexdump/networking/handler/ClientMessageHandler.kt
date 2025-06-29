package io.github.jojotastic777.hexdump.networking.handler

import dev.architectury.networking.NetworkManager.PacketContext
import io.github.jojotastic777.hexdump.config.HexdumpConfig
import io.github.jojotastic777.hexdump.networking.msg.*

fun HexdumpMessageS2C.applyOnClient(ctx: PacketContext) = ctx.queue {
    when (this) {
        is MsgSyncConfigS2C -> {
            HexdumpConfig.onSyncConfig(serverConfig)
        }

        // add more client-side message handlers here
    }
}
