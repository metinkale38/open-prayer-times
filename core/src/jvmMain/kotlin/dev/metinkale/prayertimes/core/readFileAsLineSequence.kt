package dev.metinkale.prayertimes.core

import java.io.File


private val RESOURCES_PATH = System.getenv("RESOURCES_PATH")
internal actual fun readFileAsLineSequence(filePath: String): Sequence<String> = run {
    if (RESOURCES_PATH != null && RESOURCES_PATH.isNotEmpty())
        File("$RESOURCES_PATH$filePath").bufferedReader(Charsets.UTF_8)
    else
        Entry::class.java.getResourceAsStream(filePath)!!.bufferedReader(Charsets.UTF_8)
}.lineSequence()
