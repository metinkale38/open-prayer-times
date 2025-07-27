package dev.metinkale.prayertimes

import dev.metinkale.prayertimes.api.api
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Application.module() {
    install(RoutingRoot) {}
    install(IgnoreTrailingSlash)
    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
            explicitNulls = false
        })
    }
    install(CORS) {
        allowHost("metinkale38.github.io")

    }

    routing {
        route("/api", Route::api) //deprecated
        api()
    }
}
