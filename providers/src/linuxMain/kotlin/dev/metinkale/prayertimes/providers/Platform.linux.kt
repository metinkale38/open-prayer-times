package dev.metinkale.prayertimes.providers

import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import platform.posix.*

internal actual val Platform: IPlatform = object : IPlatform {
    override fun getEnv(name: String): String? = getenv(name)?.toKString()


    val resourcePath: String by lazy {
        val bufferSize = 4096
        val buffer = ByteArray(bufferSize)
        val len = readlink("/proc/self/exe", buffer.refTo(0), bufferSize.toULong()).toInt()
        if (len <= 0) error("Could not determine resource path.")
        val exePath = buffer.decodeToString(0, len)
        exePath.substringBeforeLast('/')
    }


    override fun readFile(path: String): Sequence<String> {

        val file = fopen("$resourcePath/${path.trimStart('/')}", "r") ?: error("Cannot open file: $path")

        return sequence {
            try {
                val buffer = ByteArray(1024)
                while (true) {
                    val line = fgets(buffer.refTo(0), buffer.size, file)?.toKString()
                        ?: break
                    yield(line.trimEnd('\n', '\r'))
                }
            } finally {
                fclose(file)
            }
        }
    }
}