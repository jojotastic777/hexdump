package io.github.jojotastic777.hexdump.networking.msg

import net.minecraft.network.FriendlyByteBuf
import io.github.jojotastic777.hexdump.PatternDump.DumpEntry

data class PatternDumpS2C(val patterns: List<DumpEntry>) : HexdumpMessageS2C {
    companion object : HexdumpMessageCompanion<PatternDumpS2C> {
        override val type = PatternDumpS2C::class.java

        override fun decode(buf: FriendlyByteBuf) = PatternDumpS2C(
//            patterns = buf.readMap<Int, DumpEntry>(
//                { b -> b.readInt() },
//                { b -> DumpEntry.decode(b) }
//            )
            patterns = buf.readList<DumpEntry>({ b -> DumpEntry.decode(b) })
        )

        override fun PatternDumpS2C.encode(buf: FriendlyByteBuf) {
//            buf.writeMap<Int, DumpEntry>(
//                patterns,
//                { b, key -> b.writeInt(key) },
//                { b, value -> value.encode(b) }
//            )
            buf.writeCollection<DumpEntry>(patterns) { b, e -> e.encode(b) }
        }
    }
}