package dev.metinkale.prayertimes.core.router

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.sources.Source
import dev.metinkale.prayertimes.core.sources.features.CityListFeature
import kotlinx.serialization.Serializable


@Serializable
sealed interface ListResponse {
    @Serializable
    data class Result(val entry: Entry) : ListResponse

    @Serializable
    data class Items(val items: List<String>) : ListResponse
}

val list = HttpHandler<ListResponse> {
    fun Entry.buildResponse(): Response<ListResponse> = Response(200, ListResponse.Result(this))
    fun List<String>.buildResponse(): Response<ListResponse> =
        Response(200, ListResponse.Items(this))

    val lang = headers["accept-language"]?.split(",")?.map {
        it.substringBefore(',').substringBefore('-')
    } ?: listOf("en", "tr", "de")

    if (pathParts.isEmpty()) {
        Source.values().mapNotNull { it as? CityListFeature }.map { it.name }.buildResponse()
    } else {
        val source =
            Source.valueOf(pathParts[0]) as? CityListFeature ?: return@HttpHandler Response(404)
        if (pathParts.size == 1) {
            source.getCities().map { it.country }.distinct().toList().buildResponse()
        } else {
            val country = pathParts[1]
            val parts = pathParts.drop(2)


            val entries = source.getCities().filter { it.country == country }
                .map { it.names(*lang.toTypedArray()).reversed() to it }
                .filter { (it, _) ->
                    parts.withIndex().all { (index, name) -> it.getOrNull(index) == name }
                }.toList()

            if (entries.size == 1) {
                entries.first().second.buildResponse()
            } else entries.map { it.first.drop(parts.size).first() }.distinct().buildResponse()
        }

    }
}
