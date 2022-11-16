package dev.metinkale.prayertimes.core.utils

import dev.metinkale.prayertimes.core.Configuration
import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.readFileAsLineSequence
import dev.metinkale.prayertimes.core.sources.Source


private val entryCache = mutableMapOf<Source, List<Entry>>()

internal fun loadEntries(source: Source): Sequence<Entry> {
    return if (Configuration.HOT_ENTRIES) {
        readFileAsLineSequence("/tsv/${source.name}.tsv")
            .map { line -> Entry.decodeFromString(source, line) }
    } else {
        entryCache.getOrPut(source) {
            readFileAsLineSequence("/tsv/${source.name}.tsv")
                .map { line -> Entry.decodeFromString(source, line) }.toList()
        }.asSequence()
    }
}

