package dev.metinkale.prayertimes.core.utils

import dev.metinkale.prayertimes.core.Entry
import java.io.BufferedReader
import java.io.File



private val RESOURCES_PATH = System.getenv("RESOURCES_PATH")
internal fun readFile(filePath: String): BufferedReader = run {
    if (RESOURCES_PATH != null && RESOURCES_PATH.isNotEmpty())
        File("$RESOURCES_PATH$filePath").bufferedReader(Charsets.UTF_8)
    else
        Entry::class.java.getResourceAsStream(filePath)!!.bufferedReader(Charsets.UTF_8)
}
