package dev.metinkale.prayertimes.api

import dev.metinkale.prayertimes.db.TimesDatabase
import dev.metinkale.prayertimes.dto.DayTimesDTO
import dev.metinkale.prayertimes.dto.EntryDTO
import dev.metinkale.prayertimes.providers.Entry
import dev.metinkale.prayertimes.providers.SearchEntry
import dev.metinkale.prayertimes.providers.sources.Source
import dev.metinkale.prayertimes.providers.sources.features.CityListFeature
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDate

private val REGEX_DATE =
    "([0-9]{4})[\\-](1[0-2]|0[1-9]|[1-9])[\\-](3[01]|[12][0-9]|0[1-9]|[1-9])$".toRegex()

private operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)

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

            val endsWithDate = last == "csv" || last == "today" || REGEX_DATE.matches(path.lastOrNull() ?: "")
            if (endsWithDate) path = path.dropLast(1)

            val languages = call.request.acceptLanguageItems().map { it.value.split("-")[0] }.distinct()

            val (list, entry) = source.list(path, languages)

            if (endsWithDate && last != null) {
                handleTime(entry ?: throw NotFoundException(), last)
            } else {
                entry?.let { call.respond(EntryDTO.from(it)) } ?: list?.takeIf { list.isNotEmpty() }
                    ?.let { call.respond(it) }
                ?: throw NotFoundException()
            }
        }
    }
}


private suspend fun RoutingContext.handleTime(city: Entry, path: String) {
    val (from, to, minTimes) = when (path) {
        "csv" -> Triple(LocalDate.now(), null, 28)
        "today" -> LocalDate.now().let { Triple(it, it, 1) }
        in REGEX_DATE -> LocalDate.parse(path).let { Triple(it, it, 1) }
        else -> throw NotFoundException()
    }
    var times = TimesDatabase.get(city.source, city.id, from, to ?: LocalDate.MAX)
    if (times.size < minTimes) {
        times = city.source.getDayTimes(city.id)
            .also { TimesDatabase.persist(city.source, city.id, *it.toTypedArray()) }
            .filter { it.date >= from.toKotlinLocalDate() && (to == null || it.date <= to.toKotlinLocalDate()) }
    }

    if (path == "csv") {
        val csv = (listOf("Date;Fajr;Shuruq;Dhuhr;Asr;Maghrib;Ishaa")
                + times.map { "${it.date};${it.fajr};${it.sun};${it.dhuhr};${it.asr};${it.maghrib};${it.ishaa}" }
                ).joinToString("\n")
        call.respondText(csv,contentType = ContentType.Text.CSV)
    } else {
        call.respond(times.map { DayTimesDTO.from(it) })
    }
}

