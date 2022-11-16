package dev.metinkale.prayertimes.core.sources.features

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.GeocoderResult
import dev.metinkale.prayertimes.core.sources.Source
import dev.metinkale.prayertimes.core.utils.normalize
import kotlin.math.abs

interface CityListFeature : Source, ByLocationFeature, SearchFeature {
    fun getCities(): Sequence<Entry>

    override suspend fun search(query: String): List<Entry> {
        val words = query.normalize().split(' ')
        var bestEntry: Entry? = null
        var bestScore = 0
        for (entry in getCities()) {
            val normalizedNames = entry.normalizedNames
            val score = calculateSearchScore(words, normalizedNames)
            if (score > bestScore) {
                bestEntry = entry
                bestScore = score
            }
        }
        return listOfNotNull(bestEntry)
    }

    fun calculateSearchScore(normalizedQueries: Collection<String>, normalizedNames: Collection<String>) =
        normalizedQueries.sumOf { lhs ->
            normalizedNames.indexOf(lhs).let {
                if (it < 0) 0
                else normalizedNames.size - it
            }
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

        return listOfNotNull(bestMatch)
    }
}