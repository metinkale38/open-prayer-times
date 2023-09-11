package dev.metinkale.prayertimes.core.router

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.geo.Geocoder
import dev.metinkale.prayertimes.core.sources.Source
import dev.metinkale.prayertimes.core.sources.features.ByLocationFeature
import dev.metinkale.prayertimes.core.sources.features.SearchFeature

val search = HttpHandler<List<Entry>> {
    val query = params["q"]
    val lat = params["lat"]?.toDouble()
    val lng = params["lng"]?.toDouble()
    val lang = headers["accept-language"]?.split(",")?.map {
        it.substringBefore(',').substringBefore('-')
    } ?: listOf("en", "tr", "de")

    if (query != null && lat == null && lng == null) {

        val geo = Geocoder.searchByName(query, lang.first())

        val results: List<Entry> = Source.values().parallelMap { source ->
            (source as? SearchFeature)?.search(query, lang)?.copy(timeZone = geo?.timezone)
                ?: geo?.let { source as? ByLocationFeature }?.search(geo, lang)
        }.filterNotNull()
        Response(200, results)
    } else if (query == null && lat != null && lng != null) {
        val geo = Geocoder.byLocation(lat, lng, lang.first())
        if (geo == null) Response(500, error = "reverse geocoding did not work")
        else {
            val results = Source.values().mapNotNull { it as? ByLocationFeature }
                .parallelMap { it.search(geo, lang) }.filterNotNull()
            Response(200, results)
        }
    } else Response(400, error = "you must either set 'lat' and 'lng' or only 'q'")
}


