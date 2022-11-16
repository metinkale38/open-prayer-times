package dev.metinkale.prayertimes.core

internal expect fun readFileAsLineSequence(filePath: String): Sequence<String>
