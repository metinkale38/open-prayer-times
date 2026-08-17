package dev.metinkale.openprayertimes.server.api

import dev.metinkale.openprayertimes.City
import dev.metinkale.openprayertimes.DayTimes
import dev.metinkale.openprayertimes.Provider
import dev.metinkale.openprayertimes.hijri.now
import dev.metinkale.openprayertimes.server.db.table.TimesTable
import dev.metinkale.openprayertimes.server.feature.ListableProvider
import dev.metinkale.openprayertimes.server.provider.PrayerTimesFetcher
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.daysUntil

private val today get() = LocalDate.now()
private val lastOfYear get() = LocalDate(today.year, Month.DECEMBER, 31)

private suspend fun resolveCity(path: List<String>): City {
    return ListableProvider.getCity(path.map { it.replace("+", " ") } ) ?: throw NotFoundException("City not found")
}

suspend fun RoutingContext.handleCSV(path: List<String>) {
    val city = resolveCity(path)
    val times = getPrayerTimes(city.provider, city.id, today, lastOfYear)

    val csv = (listOf("Date;Fajr;Shuruq;Dhuhr;Asr;Maghrib;Ishaa")
            + times.map { "${it.date};${it.fajr};${it.sun};${it.dhuhr};${it.asr};${it.maghrib};${it.ishaa}" }
            ).joinToString("\n")
    call.response.header(HttpHeaders.ContentDisposition, "inline; filename=\"${city.names.first()}}.csv\"")
    call.respondText(csv, contentType = ContentType.Text.CSV)
}

suspend fun RoutingContext.handleJSON(path: List<String>) {
    val city = resolveCity(path)
    val times = getPrayerTimes(city.provider, city.id, today, lastOfYear)
    call.respond(times)
}

suspend fun RoutingContext.handleToday(path: List<String>) {
    val city = resolveCity(path)
    val times = getPrayerTimes(city.provider, city.id, today, today)
    call.respond(times)
}

suspend fun RoutingContext.handleDate(path: List<String>, date: String) {
    val city = resolveCity(path)
    val date = LocalDate.parse(date)
    val times = getPrayerTimes(city.provider, city.id, date, date)
    call.respond(times)

}

private suspend fun getPrayerTimes(
    provider: Provider,
    key: String,
    from: LocalDate,
    to: LocalDate
): List<DayTimes> {
    var times = TimesTable.get(provider, key, from, to)
    if (times.size < from.daysUntil(to) + 1) {
        times = PrayerTimesFetcher.find(provider).getPrayerTimes(key)
            .also { TimesTable.save(provider, key, it) }
            .filter { it.date >= from && (it.date <= to) }
    }
    return times
}
