package dev.metinkale.hijri

import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import kotlinx.datetime.LocalDate
import platform.posix.fclose
import platform.posix.fgets
import platform.posix.fopen
import platform.posix.readlink

internal actual val Platform: IPlatform = object : IPlatform {
    override fun convertFromHijri(date: HijriDate): LocalDate {
        TODO("Not yet implemented")
    }

    override fun convertToHijri(date: LocalDate): HijriDate {
        TODO("Not yet implemented")
    }

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