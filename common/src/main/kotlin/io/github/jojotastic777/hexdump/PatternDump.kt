package io.github.jojotastic777.hexdump

import com.google.gson.Gson
import net.minecraft.network.FriendlyByteBuf
import java.io.File

object PatternDump {
    fun onPatternDump(patterns: Map<String, DumpEntry>) {
        val gson = Gson()
        val file = File("net-patterns.json")
        file.writeText(gson.toJson(patterns))
    }

    data class DumpEntry(
        val id: String,
        val name: String,
        val startDir: String,
        val angles: String,
        val isPerWorld: Boolean
    ) {
        fun encode(buf: FriendlyByteBuf) {
            buf.writeUtf(id)
            buf.writeUtf(name)
            buf.writeUtf(startDir)
            buf.writeUtf(angles)
            buf.writeBoolean(isPerWorld)
        }

        companion object {
            fun decode(buf: FriendlyByteBuf) = DumpEntry(
                id = buf.readUtf(),
                name = buf.readUtf(),
                startDir = buf.readUtf(),
                angles = buf.readUtf(),
                isPerWorld = buf.readBoolean()
            )
        }
    }
}