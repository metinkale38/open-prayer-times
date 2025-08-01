package dev.metinkale.prayertimes.api

import dev.metinkale.hijri.now
import dev.metinkale.prayertimes.db.DatabaseHelper
import dev.metinkale.prayertimes.dto.DayTimesDTO
import dev.metinkale.prayertimes.dto.EntryDTO
import dev.metinkale.prayertimes.providers.Entry
import dev.metinkale.prayertimes.providers.SearchEntry
import dev.metinkale.prayertimes.providers.sources.Source
import dev.metinkale.prayertimes.providers.sources.features.IterableSource
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.daysUntil

private val REGEX_DATE =
    "([0-9]{4})[\\-](1[0-2]|0[1-9]|[1-9])[\\-](3[01]|[12][0-9]|0[1-9]|[1-9])$".toRegex()

private operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)

fun Route.timesApi() {
    route("/") {
        handle {
            call.respond(Source.values().map { it.name })
        }
    }
    Source.values().mapNotNull { it as? IterableSource }.forEach {
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


private fun listCities(source: IterableSource): Route.() -> Unit = {
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
    val (from, to) = when (path) {
        "csv" -> LocalDate.now().let { it to LocalDate(it.year, Month.DECEMBER, 31) }
        "today" -> LocalDate.now().let { it to it }
        in REGEX_DATE -> LocalDate.parse(path).let { it to it }
        else -> throw NotFoundException()
    }
    var times = DatabaseHelper.get(city.source, city.id, from, to)
    if (times.size < from.daysUntil(to) + 1) {
        times = city.source.getDayTimes(city.id)
            .also { DatabaseHelper.persist(city.source, city.id, *it.toTypedArray()) }
            .filter { it.date >= from && (it.date <= to) }
    }

    if (path == "csv") {
        val csv = (listOf("Date;Fajr;Shuruq;Dhuhr;Asr;Maghrib;Ishaa")
                + times.map { "${it.date};${it.fajr};${it.sun};${it.dhuhr};${it.asr};${it.maghrib};${it.ishaa}" }
                ).joinToString("\n")
        call.response.header(HttpHeaders.ContentDisposition, "inline; filename=\"${city.localizedName}.csv\"")
        call.respondText(csv, contentType = ContentType.Text.CSV)
    } else {
        call.respond(times.map { DayTimesDTO.from(it) })
    }
}

