package io.github.jojotastic777.hexdump.networking.msg

import io.github.jojotastic777.hexdump.config.HexdumpConfig
import net.minecraft.network.FriendlyByteBuf

data class MsgSyncConfigS2C(val serverConfig: HexdumpConfig.ServerConfig) : HexdumpMessageS2C {
    companion object : HexdumpMessageCompanion<MsgSyncConfigS2C> {
        override val type = MsgSyncConfigS2C::class.java

        override fun decode(buf: FriendlyByteBuf) = MsgSyncConfigS2C(
            serverConfig = HexdumpConfig.ServerConfig.decode(buf),
        )

        override fun MsgSyncConfigS2C.encode(buf: FriendlyByteBuf) {
            serverConfig.encode(buf)
        }
    }
}
