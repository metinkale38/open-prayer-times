package com.metinkale.prayertimes.core.sources

import com.metinkale.prayertimes.core.Entry

fun readTSV(source: Source, tsvFile: String): Sequence<Entry> {
    return Source::class.java.getResourceAsStream(tsvFile)!!.bufferedReader(Charsets.UTF_8)
        .lineSequence().map { line ->
            Entry.decodeFromString(source, line)
        }
}