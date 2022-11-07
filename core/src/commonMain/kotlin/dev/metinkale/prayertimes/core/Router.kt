package dev.metinkale.prayertimes.core

import dev.metinkale.prayertimes.core.routing.Request
import dev.metinkale.prayertimes.core.routing.Response
import dev.metinkale.prayertimes.core.routing.encodeResponseBody
import dev.metinkale.prayertimes.core.routing.route
import dev.metinkale.prayertimes.core.sources.Source
import dev.metinkale.prayertimes.core.sources.features.ByLocationFeature
import dev.metinkale.prayertimes.core.sources.features.DayTimesFeature
import dev.metinkale.prayertimes.core.sources.features.SearchFeature
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

object Router {

    suspend operator fun invoke(path: String) = route(Request.fromURL(path))

    suspend fun route(request: Request): Response<String> = request.route(
        "search" to { search().encodeResponseBody() },
        "times" to { times().encodeResponseBody() }
    )

    private suspend fun Request.search(): Response<List<Entry>> {
        val query = params["q"]
        val lat = params["lat"]?.toDouble()
        val lng = params["lng"]?.toDouble()

        return if (query != null && lat == null && lng == null) {
            val geo = Geocoder.search(query, languages.first())
            Response(200,
                Source.values().parallelMap {
                    (it as? SearchFeature)?.search(query)
                        ?: geo?.let { geo -> (it as? ByLocationFeature)?.search(geo) } ?: emptyList()
                }.flatten())
        } else if (query == null && lat != null && lng != null) {
            val geo = Geocoder.reverse(lat, lng, languages.first())
            if (geo == null) Response(500, error = "reverse geocoding did not work")
            else Response(200,
                Source.values().mapNotNull { it as? ByLocationFeature }.parallelMap { it.search(geo) }.flatten()
            )
        } else Response(400, error = "you must either set 'lat' and 'lng' or only 'q'")
    }


    private suspend fun Request.times(): Response<List<DayTimes>> {
        val source: DayTimesFeature? = path.getOrNull(0)?.let { Source.valueOf(it) as? DayTimesFeature }
        val id = path.getOrNull(1)
        return source?.let {
            id?.let { Response(200, source.getDayTimes(id)) }
        } ?: Response(404)
    }
}

suspend fun <F, T> List<F>.parallelMap(block: suspend (F) -> T) = map {
    GlobalScope.async { block.invoke(it) }
}.map { it.await() }