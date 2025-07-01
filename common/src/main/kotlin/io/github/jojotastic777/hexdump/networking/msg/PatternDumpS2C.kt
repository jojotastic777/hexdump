package io.github.jojotastic777.hexdump.networking.msg

import net.minecraft.network.FriendlyByteBuf
import io.github.jojotastic777.hexdump.PatternDump.DumpEntry

data class PatternDumpS2C(val patterns: Map<String, DumpEntry>) : HexdumpMessageS2C {
    companion object : HexdumpMessageCompanion<PatternDumpS2C> {
        override val type = PatternDumpS2C::class.java

        override fun decode(buf: FriendlyByteBuf) = PatternDumpS2C(
            patterns = buf.readMap<String, DumpEntry>(
                { b -> b.readUtf() },
                { b -> DumpEntry.decode(b) }
            )
        )

        override fun PatternDumpS2C.encode(buf: FriendlyByteBuf) {
            buf.writeMap<String, DumpEntry>(
                patterns,
                { b, key -> b.writeUtf(key) },
                { b, value -> value.encode(b) }
            )
        }
    }
}