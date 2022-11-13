package dev.metinkale.prayertimes.core.sources.features

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.GeocoderResult
import dev.metinkale.prayertimes.core.readFileAsLineSequence
import dev.metinkale.prayertimes.core.sources.Source
import dev.metinkale.prayertimes.core.utils.dropRows
import dev.metinkale.prayertimes.core.utils.levenshtein
import dev.metinkale.prayertimes.core.utils.normalize
import kotlin.math.abs

interface CityListFeature : Source, ByLocationFeature, SearchFeature {

    fun getCitiesTSV(): Sequence<String> = readFileAsLineSequence("/tsv/${name}.tsv")
    fun getCities(): Sequence<Entry> = getCitiesTSV().map { line -> Entry.decodeFromString(this, line) }


    override suspend fun search(query: String): List<Entry> {

        val words = query.normalize().split(" ")
        var bestScore = -(5 * words.size)
        var bestEntry: String? = null

        for (entry in getCitiesTSV()) {
            val names = entry.dropRows(4).normalize().split(" ")
            val score = words.sumOf { lhs ->
                names.mapIndexed { index, it -> index to it }.maxOf { (index, rhs) ->
                    -(levenshtein(lhs, rhs) + index)
                }
            }
            if (score > bestScore) {
                bestScore = score
                bestEntry = entry
            }
        }
        return listOfNotNull(bestEntry?.let { Entry.decodeFromString(this, it) }?.withTimeZone())
    }

    override suspend fun search(geolocation: GeocoderResult): List<Entry> {
        val lat = geolocation.lat
        val lng = geolocation.lng
        var bestMatch: Entry? = null

        for (entry in getCities()) {
            if (entry.lat != null && entry.lng != null) {
                val latDist = abs(lat - entry.lat)
                val lngDist = abs(lng - entry.lng)
                if (bestMatch == null) {
                    if (latDist < 2 && lngDist < 2) bestMatch = entry
                } else {
                    if (latDist + lngDist < abs(lat - bestMatch.lat!!) + abs(lng - bestMatch.lng!!)) {
                        bestMatch = entry
                    }
                }
            }
        }

        return listOfNotNull(bestMatch?.withTimeZone())
    }
}