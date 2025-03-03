package dev.metinkale.prayertimes

import dev.metinkale.prayertimes.api.api
import dev.metinkale.prayertimes.providers.Configuration
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main() {
    Configuration.LOW_MEMORY_MODE = false

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
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

    routing {
        route("/api", Route::api)
    }
}
