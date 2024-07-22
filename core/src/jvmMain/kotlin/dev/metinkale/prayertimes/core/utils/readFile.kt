package dev.metinkale.prayertimes.core.utils

import dev.metinkale.prayertimes.core.Entry
import java.io.BufferedReader
import java.io.File

internal fun readFile(filePath: String): BufferedReader = run {
    Entry::class.java.getResourceAsStream(filePath)!!.bufferedReader(Charsets.UTF_8)
}
