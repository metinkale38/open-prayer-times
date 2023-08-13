package dev.metinkale.prayertimes.core.router

import dev.metinkale.prayertimes.core.sources.Source
import dev.metinkale.prayertimes.core.sources.features.DayTimesFeature

val times = HttpHandler {
    val source: DayTimesFeature? = pathParts.getOrNull(0)?.let { Source.valueOf(it) }
    val id = pathParts.getOrNull(1)
    source?.let {
        id?.let { Response(200, source.getDayTimes(id)) }
    } ?: Response(404)
}

