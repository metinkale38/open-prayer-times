package dev.metinkale.prayertimes.providers.utils

import dev.metinkale.prayertimes.providers.Entry
import java.io.BufferedReader

internal fun readFile(filePath: String): BufferedReader = run {
    Entry::class.java.getResourceAsStream(filePath)!!.bufferedReader(Charsets.UTF_8)
}
