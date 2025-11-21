package dev.metinkale.openprayertimes.server

import dev.metinkale.openprayertimes.server.api.api
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json


fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}


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


