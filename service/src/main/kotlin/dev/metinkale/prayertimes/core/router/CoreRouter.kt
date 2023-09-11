package dev.metinkale.prayertimes.core.router

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer


val coreRouter: HttpHandler<String> = Router {
    "search" GET search.mapBody { Json.encodeToString(Json.serializersModule.serializer(), it) }
        .withContentTypeJson()
    "times" GET times.mapBody { Json.encodeToString(Json.serializersModule.serializer(), it) }
        .withContentTypeJson()
    "list" GET list.mapBody { Json.encodeToString(Json.serializersModule.serializer(), it) }
        .withContentTypeJson()
}
