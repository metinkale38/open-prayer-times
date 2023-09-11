package dev.metinkale.prayertimes.core.geo

import dev.metinkale.prayertimes.core.utils.readFile
import dev.metinkale.prayertimes.core.utils.SearchEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs


object Geocoder {
    private val geonames
        get() =
            readFile("/tsv/geonames.tsv").lineSequence()
                .map { it.split("\t") }
                .map { Geolocation(it) }


    suspend fun byLocation(lat: Double, lng: Double): Geolocation? = withContext(Dispatchers.IO) {
        var bestMatch = geonames.first()
        for (entry in geonames) {
            if (abs(lat - entry.lat) + abs(lng - entry.lng) < abs(lat - bestMatch.lat) + abs(lng - bestMatch.lng)) {
                bestMatch = entry
            }
        }
        bestMatch.takeIf { abs(lat - it.lat) < 2 && abs(lng - it.lng) < 2 }
    }

    suspend fun searchByName(q: String): Geolocation? = withContext(Dispatchers.IO) {
        SearchEngine.search(geonames, { it.normalizedNames }, q)
    }
}