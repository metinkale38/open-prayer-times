package dev.metinkale.prayertimes.providers.geo

import dev.metinkale.prayertimes.providers.Platform
import dev.metinkale.prayertimes.providers.utils.TextSearchEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.math.abs


object Geocoder {
    private val geonames
        get() =
            Platform.readFile("/tsv/cities500.txt").map { Geolocation.readFromLine(it) }

    private val allowedPPLA = listOf("PPLG", "PPLC", "PPLA", "PPLA1", "PPLA2", "PPLA3", "PPLA4", "PPLA5")

    private val onlyPPLA get() = geonames.filter { it.feature_code in allowedPPLA }


    suspend fun byLocation(lat: Double, lng: Double): Geolocation? = withContext(Dispatchers.IO) {
        var bestMatch: Geolocation? = null
        for (entry in onlyPPLA) {
            if (bestMatch == null || abs(lat - entry.lat) + abs(lng - entry.lng) < abs(lat - bestMatch.lat) + abs(
                    lng - bestMatch.lng
                )
            ) {
                bestMatch = entry
            }
        }
        bestMatch?.takeIf { abs(lat - it.lat) < 2 && abs(lng - it.lng) < 2 }
    }

    suspend fun searchByName(q: String): Geolocation? = withContext(Dispatchers.IO) {
        TextSearchEngine.search(geonames, { it.normalizedNames }, { -it.population }, q)
    }
}