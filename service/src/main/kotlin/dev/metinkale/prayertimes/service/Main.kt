package dev.metinkale.prayertimes.service

import dev.metinkale.prayertimes.core.Configuration
import dev.metinkale.prayertimes.core.features.cityListFeatureFactory
import dev.metinkale.prayertimes.core.router.HttpHandler
import dev.metinkale.prayertimes.core.router.Method
import dev.metinkale.prayertimes.core.router.Request
import dev.metinkale.prayertimes.core.router.coreRouter
import dev.metinkale.prayertimes.core.sources.features.cityListFeatureFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

fun main() {
    Configuration.IGMG_API_KEY = Secrets.IGMG_API_KEY
    Configuration.LONDON_PRAYER_TIMES_API_KEY = Secrets.LONDON_PRAYER_TIMES_API_KEY
    cityListFeatureFactory = ::cityListFeatureFactory

    embeddedServer(Netty, port = 8080) {
        install(IgnoreTrailingSlash)
        routing {
            get("{...}") {
                coreRouter.handle(call)
            }
        }
    }.start(wait = true)
}


suspend fun <T> HttpHandler<T>.handle(call: ApplicationCall) {
    val request = Request(
        Method.GET,
        call.request.path(),
        call.request.queryParameters.toMap().mapValues { it.value.first() },
        call.request.headers.toMap().mapValues { it.value.first() })
    val response = invoke(request)

    val status = HttpStatusCode.fromValue(response.status)
    response.contentType?.let { call.response.header("content-type", it) }
    call.respond(status, if (status.isSuccess()) response.body ?: "" else response.error ?: "")
}