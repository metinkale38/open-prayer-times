package dev.metinkale.prayertimes.core

actual fun readFileAsLineSequence(filePath: String): Sequence<String> =
    Entry::class.java.getResourceAsStream(filePath)!!.bufferedReader(Charsets.UTF_8).lineSequence()

