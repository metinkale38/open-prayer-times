package dev.metinkale.openprayertimes.server.api

import dev.metinkale.openprayertimes.server.feature.SearchableProvider
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.searchRoutes() {
    get("/search") {
        val lat = call.request.queryParameters["lat"]?.toFloat()
        val lng = call.request.queryParameters["lng"]?.toFloat()
        val q = call.request.queryParameters["q"]
        val entries = q?.let { SearchableProvider.search(it) }
            ?: lat?.let { lng?.let { SearchableProvider.findNearestCity(lat, lng) } }
            ?: emptyList()

        call.respond(entries)
    }
}