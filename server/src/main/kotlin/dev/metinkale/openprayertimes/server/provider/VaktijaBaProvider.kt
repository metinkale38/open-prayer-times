package dev.metinkale.openprayertimes.server.provider

import dev.metinkale.openprayertimes.DayTimes
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object VaktijaBaProvider : PrayerTimesFetcher {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getPrayerTimes(key: String): List<DayTimes> {
        val year = java.time.LocalDate.now().year
        val response = httpClient
            .get("https://api.vaktija.ba/vaktija/v1/$key/$year")
            .bodyAsText()
            .let { json.decodeFromString(Response.serializer(), it) }

        return response.mjesec.flatMapIndexed { monthIndex, month ->
            month.dan.mapIndexedNotNull { dayIndex, day ->
                if (day.vakat.size < 6) return@mapIndexedNotNull null
                val date = runCatching { LocalDate(year, monthIndex + 1, dayIndex + 1) }
                    .getOrNull() ?: return@mapIndexedNotNull null

                DayTimes(
                    date = date,
                    fajr = day.vakat[0].parseTime(),
                    sun = day.vakat[1].parseTime(),
                    dhuhr = day.vakat[2].parseTime(),
                    asr = day.vakat[3].parseTime(),
                    maghrib = day.vakat[4].parseTime(),
                    ishaa = day.vakat[5].parseTime()
                )
            }
        }.sortedBy { it.date }
    }

    private fun String.parseTime() =
        split(":").let { LocalTime(it[0].toInt(), it[1].toInt()) }

    @Serializable
    private data class Response(
        val mjesec: List<Month>
    )

    @Serializable
    private data class Month(
        val dan: List<Day>
    )

    @Serializable
    private data class Day(
        val vakat: List<String>
    )
}
