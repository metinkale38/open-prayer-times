package dev.metinkale.prayertimes.server

import dev.metinkale.prayertimes.core.Configuration
import dev.metinkale.prayertimes.core.router.HttpHandler
import dev.metinkale.prayertimes.core.router.Method
import dev.metinkale.prayertimes.core.router.Request
import dev.metinkale.prayertimes.core.router.coreRouter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

fun main() {
    Configuration.GOOGLE_API_KEY = ""
    Configuration.IGMG_API_KEY = ""
    Configuration.LONDON_PRAYER_TIMES_API_KEY = ""


    embeddedServer(Netty, port = 8080) {
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
