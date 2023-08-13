package dev.metinkale.prayertimes.core.router

import dev.metinkale.prayertimes.core.Geocoder
import dev.metinkale.prayertimes.core.Geolocation
import dev.metinkale.prayertimes.core.sources.Source
import dev.metinkale.prayertimes.core.sources.features.ByLocationFeature
import dev.metinkale.prayertimes.core.sources.features.SearchFeature
import dev.metinkale.prayertimes.core.utils.parallelMap
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.abs

val search = HttpHandler {
    val query = params["q"]
    val lat = params["lat"]?.toDouble()
    val lng = params["lng"]?.toDouble()
    val lang = headers["accept-language"]?.split(",")?.map {
        it.substringBefore(',').substringBefore('-')
    } ?: listOf("en", "tr", "de")

    if (query != null && lat == null && lng == null) {

        var geolocation: Geolocation? = null
        var geolocated = false
        val geolock = Mutex(false)

        suspend fun geo() = if (geolocated) geolocation else {
            geolock.withLock {
                if (geolocated) geolocation else {
                    geolocation = runCatching { Geocoder.search(query, lang.first()) }.getOrNull()
                    geolocated = true
                    geolocation
                }
            }
        }

        val results = Source.values().parallelMap { source ->
            source to run {
                (source as? SearchFeature)?.search(query)?.ifEmpty { null }
                    ?: (source as? ByLocationFeature)?.run { geo()?.let { search(it) } } ?: emptyList()
            }.map { it.withTimeZone() }
        }.map { (source, entries) ->
            if (source == Source.Calc || entries.size <= 1) entries
            else (geo()?.let { geo -> // take entry, which is nearest to the geolocated position
                entries.sortedBy { abs(geo.lat - (it.lat ?: 0.0)) + abs(geo.lng - (it.lng ?: 0.0)) }
            } ?: entries).subList(0, 1)
        }.flatten()
        Response(200, results)
    } else if (query == null && lat != null && lng != null) {
        val geo = Geocoder.reverse(lat, lng, lang.first())
        if (geo == null) Response(500, error = "reverse geocoding did not work")
        else {
            val results = Source.values().mapNotNull { it as? ByLocationFeature }
                .parallelMap { it.search(geo).map { it.withTimeZone() } }
                .flatten()
            Response(200, results)
        }
    } else Response(400, error = "you must either set 'lat' and 'lng' or only 'q'")
}


