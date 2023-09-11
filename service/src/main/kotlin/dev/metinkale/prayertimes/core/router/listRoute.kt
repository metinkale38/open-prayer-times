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
    System.err.println(pathParts)

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

        source.list(pathParts.drop(1),lang).let {
            it.first?.buildResponse() ?: it.second?.buildResponse() ?:  return@HttpHandler Response(404)
        }

    }
}
