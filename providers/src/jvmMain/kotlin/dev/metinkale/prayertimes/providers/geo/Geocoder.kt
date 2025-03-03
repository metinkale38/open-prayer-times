package dev.metinkale.prayertimes.providers.geo

import dev.metinkale.prayertimes.providers.utils.TextSearchEngine
import dev.metinkale.prayertimes.providers.utils.readFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.jvm.optionals.getOrNull
import kotlin.math.abs


object Geocoder {
    private val geonames
        get() =
            readFile("/tsv/cities500.txt").lineSequence()
                .map { Geolocation.readFromLine(it) }

    private val allowedPPLA = listOf("PPLG", "PPLC", "PPLA", "PPLA1", "PPLA2", "PPLA3", "PPLA4", "PPLA5")

    private val onlyPPLA get() = geonames.filter { it.feature_code in allowedPPLA }


    suspend fun byLocation(lat: Double, lng: Double): Geolocation? =
        withContext(Dispatchers.IO) {
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
        }.let { Optional.ofNullable(it) }.getOrNull()

    suspend fun searchByName(q: String): Geolocation? = withContext(Dispatchers.IO) {
            TextSearchEngine.search(geonames, { it.normalizedNames }, { -it.population }, q)
        }.let { Optional.ofNullable(it) }.getOrNull()
}