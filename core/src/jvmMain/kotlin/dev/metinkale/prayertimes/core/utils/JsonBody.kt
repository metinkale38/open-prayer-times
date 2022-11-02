package dev.metinkale.prayertimes.core

import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.http4k.core.Response
import java.nio.charset.Charset


val json = Json


inline fun <reified T> Response.body(body: T): Response =
    header(
        "Content-Type",
        "application/json; charset=utf-8"
    ).body(
        json.encodeToString(Json.serializersModule.serializer(), body)
            .byteInputStream(Charset.forName("UTF-8"))
    )

inline fun <reified T> Response.body(body: List<T>): Response =
    header(
        "Content-Type",
        "application/json; charset=utf-8"
    ).body(
        json.encodeToString(ListSerializer(Json.serializersModule.serializer()), body)
            .byteInputStream(Charset.forName("UTF-8"))
    )