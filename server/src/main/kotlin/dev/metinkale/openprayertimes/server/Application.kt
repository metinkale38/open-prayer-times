package dev.metinkale.openprayertimes.server

import dev.metinkale.openprayertimes.server.RequestContext.Companion.withLanguages
import dev.metinkale.openprayertimes.server.api.routes
import dev.metinkale.openprayertimes.server.db.DatabaseFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.acceptLanguageItems
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json


fun main() {
    System.setProperty("io.ktor.development", "false")
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}


fun Application.module() {
    DatabaseFactory.init()

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
    intercept(ApplicationCallPipeline.Call) {
        val languages = call.request.acceptLanguageItems()
            .map { it.value.split("-")[0] }
            .distinct()
        withLanguages(languages) {
            proceed()
        }
    }

    routing {
        route("/api", Route::routes) //deprecated
        routes()
    }
}


