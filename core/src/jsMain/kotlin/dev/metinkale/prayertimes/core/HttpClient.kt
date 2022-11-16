package dev.metinkale.prayertimes.core

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


internal actual object HttpClient {

    val https = require("https")

    actual suspend fun get(
        url: String,
        build: HTTPClientBuilder.() -> Unit
    ): String {
        return request(url, "GET", build)
    }

    actual suspend fun post(
        url: String,
        build: HTTPClientBuilder.() -> Unit
    ): String {
        return request(url, "POST", build)
    }

    private suspend fun request(
        url: String,
        method: String,
        build: HTTPClientBuilder.() -> Unit
    ): String {
        val builder = HTTPClientBuilder().apply(build)
        return suspendCoroutine { cont ->
            val req = https.request(
                url, obj(
                    "method" to method,
                    "headers" to obj(*builder.headers.toList().toTypedArray())
                )
            ) { resp: dynamic ->
                var data = "";

                resp.on("data") { chunk: String ->
                    data += chunk
                    @Suppress("RedundantUnitExpression") Unit
                }

                resp.on("end") {
                    cont.resume(data)
                }
            }

            req.on("error") { err ->
                console.error(err)
                cont.resume("")
            }
            req.on("timeout") { err ->
                console.error(err)
                cont.resume("")
            }
            builder.body?.let { req.write(it) }
            req.end()
            Unit
        }
    }
}