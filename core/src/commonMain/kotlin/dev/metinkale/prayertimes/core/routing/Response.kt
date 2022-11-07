package dev.metinkale.prayertimes.core.routing

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer


data class Response<T>(
    val status: Int,
    val body: T? = null,
    val error: String? = null,
    val contentType: String? = null
) {
    companion object {
        val json = Json
    }
}

inline fun <reified T> Response<T>.encodeResponseBody(): Response<String> = run {
    Response(
        status,
        Response.json.encodeToString(Json.serializersModule.serializer(), body),
        contentType = "application/json"
    )
}
