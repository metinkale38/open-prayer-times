package dev.metinkale.prayertimes

import com.ucasoft.ktor.simpleCache.SimpleCache
import com.ucasoft.ktor.simpleMemoryCache.memoryCache
import dev.metinkale.prayertimes.api.api
import dev.metinkale.prayertimes.core.Configuration
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.minutes

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Routing) {}
    install(IgnoreTrailingSlash)
    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
            explicitNulls = false
        })
    }
    install(CORS) {
        allowHost("localhost:3000")
    }
    install(SimpleCache) {
        memoryCache {
            invalidateAt = 10.minutes
            Configuration.CACHE_PROVIDER = object : Configuration.CacheProvider {
                override suspend fun <T : Any> applyCache(key: String, action: suspend () -> T): T {
                    @Suppress("UNCHECKED_CAST")
                    return (provider?.getCache(key) as? T) ?: run {
                        val result: T = action.invoke()
                        provider?.setCache(key, result, invalidateAt)
                        result
                    }
                }

            }

        }

    }
    routing {
        route("/api", Route::api)
        staticResources("/", "static")
    }
}
