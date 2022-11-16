package dev.metinkale.prayertimes.core.utils

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.HOT_ENTRIES
import dev.metinkale.prayertimes.core.readFileAsLineSequence
import dev.metinkale.prayertimes.core.sources.Source



private val entryCache = mutableMapOf<Source, List<Entry>>()
fun loadEntries(source: Source): Sequence<Entry> {
    if (HOT_ENTRIES) return readFileAsLineSequence("/tsv/${source.name}.tsv")
        .map { line -> Entry.decodeFromString(source, line) }
    else return entryCache.getOrPut(source) {
        readFileAsLineSequence("/tsv/${source.name}.tsv")
            .map { line -> Entry.decodeFromString(source, line) }.toList()
    }.asSequence()
}

