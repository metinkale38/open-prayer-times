package dev.metinkale.prayertimes.api

import io.ktor.server.routing.*

fun Route.api() {
    timesApi()
    dateApi()
}

