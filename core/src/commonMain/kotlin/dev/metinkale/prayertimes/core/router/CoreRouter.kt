package dev.metinkale.prayertimes.core.router

import dev.metinkale.prayertimes.core.Geocoder
import dev.metinkale.prayertimes.core.GeocoderResult
import dev.metinkale.prayertimes.core.sources.Source
import dev.metinkale.prayertimes.core.sources.features.ByLocationFeature
import dev.metinkale.prayertimes.core.sources.features.DayTimesFeature
import dev.metinkale.prayertimes.core.sources.features.SearchFeature
import dev.metinkale.prayertimes.core.utils.parallelMap
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer


val search = HttpHandler {
    val query = params["q"]
    val lat = params["lat"]?.toDouble()
    val lng = params["lng"]?.toDouble()
    val lang = headers["accept-language"]?.split(",")?.map {
        it.substringBefore(',').substringBefore('-')
    } ?: listOf("en", "tr", "de")

    if (query != null && lat == null && lng == null) {

        var geolocation: GeocoderResult? = null
        var geolocated = false

        suspend fun geo() = if (geolocated) geolocation else {
            geolocated = true
            geolocation = Geocoder.search(query, lang.first())
            geolocation
        }

        val results = Source.values().parallelMap {
            run {
                (it as? SearchFeature)?.search(query)
                    ?: geo()?.let { geo -> (it as? ByLocationFeature)?.search(geo) }
                    ?: emptyList()
            }.map { it.withTimeZone() }
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


val times = HttpHandler {
    val source: DayTimesFeature? = pathParts.getOrNull(0)?.let { Source.valueOf(it) as? DayTimesFeature }
    val id = pathParts.getOrNull(1)
    source?.let {
        id?.let { Response(200, source.getDayTimes(id)) }
    } ?: Response(404)
}


val coreRouter: HttpHandler<String> = Router {
    "search" GET search.mapBody { Json.encodeToString(Json.serializersModule.serializer(), it) }.withContentTypeJson()
    "times" GET times.mapBody { Json.encodeToString(Json.serializersModule.serializer(), it) }.withContentTypeJson()
}
