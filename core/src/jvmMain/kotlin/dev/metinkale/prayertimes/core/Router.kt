package dev.metinkale.prayertimes.core

import dev.metinkale.prayertimes.core.sources.features.ByLocationFeature
import dev.metinkale.prayertimes.core.sources.features.SearchFeature
import dev.metinkale.prayertimes.core.sources.Source
import dev.metinkale.prayertimes.core.sources.features.DayTimesFeature
import kotlinx.coroutines.runBlocking
import org.http4k.core.*
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes

val Request.languages: List<String>
    get() = header("Accept-Language")?.split(",")?.map { it.split("-")[0].split(";")[0] }?.distinct()?.let { it - "*" }
        ?.ifEmpty { null } ?: listOf("en", "tr", "de")

val router: HttpHandler = routes(
    "/search" bind Method.GET to {
        runBlocking {

            val query = it.query("q")
            val lat = it.query("lat")?.toDouble()
            val lng = it.query("lng")?.toDouble()


            if (query != null && lat == null && lng == null) {
                val geo = Geocoder.search(query, it.languages.first())
                Response(Status.OK).body(
                    Source.values().flatMap {
                        (it as? SearchFeature)?.search(query)
                            ?: geo?.let { geo -> (it as? ByLocationFeature)?.search(geo) } ?: emptyList()
                    })
            } else if (query == null && lat != null && lng != null) {
                val geo = Geocoder.reverse(lat, lng, it.languages.first())
                if (geo == null) Response(Status.INTERNAL_SERVER_ERROR).body("reverse geocoding did not work")
                else Response(Status.OK).body(
                    Source.values().mapNotNull { it as? ByLocationFeature }.flatMap { it.search(geo) }
                )
            } else Response(Status.BAD_REQUEST).body("you must either set 'lat' and 'lng' or only 'q'")
        }
    },
    "/times/{source}/{id}" bind Method.GET to {
        val id = it.path("id")
        val source: DayTimesFeature? = Source.valueOf(it.path("source")!!) as? DayTimesFeature
        source?.let {
            id?.let {
                Response(Status.OK).body(runBlocking { source.getDayTimes(id) })
            }
        } ?: Response(Status.NOT_FOUND)
    }

)


