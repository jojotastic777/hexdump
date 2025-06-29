package io.github.jojotastic777.hexdump.networking.msg

import dev.architectury.networking.NetworkChannel
import dev.architectury.networking.NetworkManager.PacketContext
import io.github.jojotastic777.hexdump.Hexdump
import io.github.jojotastic777.hexdump.networking.HexdumpNetworking
import io.github.jojotastic777.hexdump.networking.handler.applyOnClient
import io.github.jojotastic777.hexdump.networking.handler.applyOnServer
import net.fabricmc.api.EnvType
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import java.util.function.Supplier

sealed interface HexdumpMessage

sealed interface HexdumpMessageC2S : HexdumpMessage {
    fun sendToServer() {
        HexdumpNetworking.CHANNEL.sendToServer(this)
    }
}

sealed interface HexdumpMessageS2C : HexdumpMessage {
    fun sendToPlayer(player: ServerPlayer) {
        HexdumpNetworking.CHANNEL.sendToPlayer(player, this)
    }

    fun sendToPlayers(players: Iterable<ServerPlayer>) {
        HexdumpNetworking.CHANNEL.sendToPlayers(players, this)
    }
}

sealed interface HexdumpMessageCompanion<T : HexdumpMessage> {
    val type: Class<T>

    fun decode(buf: FriendlyByteBuf): T

    fun T.encode(buf: FriendlyByteBuf)

    fun apply(msg: T, supplier: Supplier<PacketContext>) {
        val ctx = supplier.get()
        when (ctx.env) {
            EnvType.SERVER, null -> {
                Hexdump.LOGGER.debug("Server received packet from {}: {}", ctx.player.name.string, this)
                when (msg) {
                    is HexdumpMessageC2S -> msg.applyOnServer(ctx)
                    else -> Hexdump.LOGGER.warn("Message not handled on server: {}", msg::class)
                }
            }
            EnvType.CLIENT -> {
                Hexdump.LOGGER.debug("Client received packet: {}", this)
                when (msg) {
                    is HexdumpMessageS2C -> msg.applyOnClient(ctx)
                    else -> Hexdump.LOGGER.warn("Message not handled on client: {}", msg::class)
                }
            }
        }
    }

    fun register(channel: NetworkChannel) {
        channel.register(type, { msg, buf -> msg.encode(buf) }, ::decode, ::apply)
    }
}
