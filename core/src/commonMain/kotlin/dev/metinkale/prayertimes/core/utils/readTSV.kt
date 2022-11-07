package dev.metinkale.prayertimes.core.utils

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.readFileAsLineSequence
import dev.metinkale.prayertimes.core.sources.Source

fun readTSV(source: Source, tsvFile: String): Sequence<Entry> {
    return readFileAsLineSequence(tsvFile).map { line ->
        Entry.decodeFromString(source, line)
    }
}