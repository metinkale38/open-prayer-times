package dev.metinkale.prayertimes.api

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.SearchEntry
import dev.metinkale.prayertimes.core.sources.Source
import dev.metinkale.prayertimes.core.sources.features.CityListFeature
import dev.metinkale.prayertimes.dto.EntryDTO
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.datetime.LocalDate

private val REGEX_DATE =
    "([0-9]{4})[\\-](1[0-2]|0[1-9]|[1-9])[\\-](3[01]|[12][0-9]|0[1-9]|[1-9])$".toRegex()

fun Route.timesApi() {
    route("/") {
        handle {
            call.respond(Source.values().map { it.name })
        }
    }
    Source.values().mapNotNull { it as? CityListFeature }.forEach {
        route("/${it.name}/{path...}", listCities(it))
    }
    route("/search") {
        handle {
            val lat = call.request.queryParameters["lat"]?.toDouble()
            val lng = call.request.queryParameters["lng"]?.toDouble()
            val q = call.request.queryParameters["q"]
            val entries = q?.let { SearchEntry.search(it) } ?: lat?.let { lng?.let { SearchEntry.search(lat, lng) } }
            ?: emptyList()

            call.respond(entries)
        }
    }
}


private fun listCities(source: CityListFeature): Route.() -> Unit = {
    route("/") {
        handle {
            var path = call.parameters.getAll("path") ?: throw NotFoundException()
            val last = path.lastOrNull()

            val matchesTimesIdentifier = endsWithTimeIdentifier(path)
            if (matchesTimesIdentifier) path = path.dropLast(1)

            val languages = call.request.acceptLanguageItems().map { it.value.split("-")[0] }.distinct()

            val (list, entry) = source.list(path, languages)

            if (matchesTimesIdentifier && last != null) {
                handleTime(entry ?: throw NotFoundException(), last)
            } else {
                entry?.let { call.respond(EntryDTO.from(it)) } ?: list?.takeIf { list.isNotEmpty() }
                    ?.let { call.respond(it) }
                ?: throw NotFoundException()
            }
        }
    }

    route("/today") {
        handle {
            call.respond("Hallo")
        }
    }
}

private suspend fun PipelineContext<*, ApplicationCall>.handleTime(city: Entry, date: String) {
    if (date == "today") call.respond(city.source.getDayTime(city.id) ?: throw NotFoundException())
    else call.respond(city.source.getDayTime(city.id, LocalDate.parse(date)) ?: throw NotFoundException())
}

private fun endsWithTimeIdentifier(path: List<String>): Boolean {
    val last = path.lastOrNull() ?: ""
    return last == "today" || REGEX_DATE.matches(last)
}