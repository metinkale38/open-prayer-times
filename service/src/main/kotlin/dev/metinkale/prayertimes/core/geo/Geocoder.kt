package dev.metinkale.prayertimes.core.geo

import dev.metinkale.prayertimes.core.utils.readFile
import dev.metinkale.prayertimes.core.utils.SearchEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs


object Geocoder {

    val allowedFeatureCodes = listOf("PPLG", "PPLC", "PPLA", "PPLA1", "PPLA2", "PPLA3", "PPLA4", "PPL5", "PPL")

    private val geonames
        get() =
            readFile("/geonames/cities5000.txt").lineSequence()
                .map { it.split("\t") }
                .filter { it[7] in allowedFeatureCodes }
                .map { GeonamesLine(it) }


    suspend fun byLocation(lat: Double, lng: Double, lang: String = ""): Geolocation? = withContext(Dispatchers.IO) {
        // TODO localized names
        var bestMatch: GeonamesLine = geonames.first()
        for (entry in geonames) {
            if (abs(lat - entry.lat) + abs(lng - entry.lng) < abs(lat - bestMatch.lat) + abs(lng - bestMatch.lng)) {
                bestMatch = entry
            }
        }
        bestMatch.takeIf { abs(lat - it.lat) < 2 && abs(lng - it.lng) < 2 }?.toGeolocation()
    }

    suspend fun searchByName(q: String, lang: String = ""): Geolocation? = withContext(Dispatchers.IO) {
        // TODO localized names
        SearchEngine.search(geonames, { it.normalizedNames }, q)?.toGeolocation()
    }
}