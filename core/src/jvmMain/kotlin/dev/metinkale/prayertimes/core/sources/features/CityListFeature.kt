package dev.metinkale.prayertimes.core.sources.features

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.Geocoder
import dev.metinkale.prayertimes.core.sources.Source
import dev.metinkale.prayertimes.core.utils.normalize
import kotlin.math.abs

interface CityListFeature : Source, ByLocationFeature, SearchFeature {
    fun getCities(): Sequence<Entry>

    override suspend fun search(query: String): List<Entry> {
        val words = query.normalize().split(" ")
        var bestScope = 0
        var bestEntry: Entry? = null
        getCities().forEach { entry ->
            val score = entry.names.mapIndexed { index, map -> index to map }.sumOf { (index, map) ->
                if (words.all { word -> map.values.joinToString(" ").normalize().contains(word) })
                    10 - index
                else 0
            }
            if (score > bestScope) {
                bestScope = score
                bestEntry = entry
            }
        }
        return listOfNotNull(bestEntry?.withTimeZone())
    }

    override suspend fun search(geolocation: Geocoder.GeoLocation): List<Entry> {
        val lat = geolocation.lat
        val lng = geolocation.lng
        var bestMatch: Entry? = null
        getCities().filter { it.lat != null && it.lng != null }.forEach { entry ->
            val latDist = abs(lat - entry.lat!!)
            val lngDist = abs(lng - entry.lng!!)
            if (bestMatch == null) {
                if (latDist < 2 && lngDist < 2) bestMatch = entry
            } else {
                if (latDist + lngDist < abs(lat - bestMatch!!.lat!!) + abs(lng - bestMatch!!.lng!!)) {
                    bestMatch = entry
                }
            }
        }

        return listOfNotNull(bestMatch?.withTimeZone())
    }
}