package dev.metinkale.prayertimes.core.utils

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.sources.Source

fun readTSV(source: Source, tsvFile: String): Sequence<Entry> {
    return Source::class.java.getResourceAsStream(tsvFile)!!.bufferedReader(Charsets.UTF_8)
        .lineSequence().map { line ->
            Entry.decodeFromString(source, line)
        }
}