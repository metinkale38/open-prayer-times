package com.metinkale.prayertimes.core

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.http4k.core.Response


val json = Json


inline fun <reified T> Response.body(body: T) =
    body(json.encodeToString(Json.serializersModule.serializer(), body))

inline fun <reified T> Response.body(body: List<T>) =
    body(json.encodeToString(ListSerializer(Json.serializersModule.serializer()), body))