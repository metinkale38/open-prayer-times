package dev.metinkale.openprayertimes.server.api

import io.ktor.server.routing.*

fun Route.  api() {
    timesApi()
    dateApi()
}

