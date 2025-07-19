package io.github.jojotastic777.hexdump

import com.google.gson.Gson
import net.minecraft.network.FriendlyByteBuf
import java.io.File

object PatternDump {
    fun onPatternDump(patterns: List<DumpEntry>) {
        val gson = Gson()
        val file = File("patterns.json")
        file.writeText(gson.toJson(patterns))
    }

    data class DumpEntry(
        val id: String,
        val name: String,
        val startdir: String,
        val signature: String,
        val is_per_world: Boolean
    ) {
        fun encode(buf: FriendlyByteBuf) {
            buf.writeUtf(id)
            buf.writeUtf(name)
            buf.writeUtf(startdir)
            buf.writeUtf(signature)
            buf.writeBoolean(is_per_world)
        }

        companion object {
            fun decode(buf: FriendlyByteBuf) = DumpEntry(
                id = buf.readUtf(),
                name = buf.readUtf(),
                startdir = buf.readUtf(),
                signature = buf.readUtf(),
                is_per_world = buf.readBoolean()
            )
        }
    }
}