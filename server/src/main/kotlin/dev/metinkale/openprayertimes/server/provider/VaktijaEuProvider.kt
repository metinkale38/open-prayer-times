package dev.metinkale.openprayertimes.server.provider

import dev.metinkale.openprayertimes.DayTimes
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object VaktijaEuProvider : PrayerTimesFetcher {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getPrayerTimes(key: String): List<DayTimes> {
        val year = java.time.LocalDate.now().year
        val response = httpClient
            .get("https://api.vaktija.eu/v3/locations/slug/$key")
            .bodyAsText()
            .let { json.decodeFromString(Response.serializer(), it) }

        return response.data.months.values
            .flatMap { it.days.values }
            .mapNotNull { day ->
                val date = runCatching { LocalDate(year, day.month, day.day) }.getOrNull()
                    ?: return@mapNotNull null

                DayTimes(
                    date = date,
                    fajr = LocalTime.parse(day.fajr),
                    sun = LocalTime.parse(day.sunrise),
                    dhuhr = LocalTime.parse(day.dhuhr),
                    asr = LocalTime.parse(day.asr),
                    maghrib = LocalTime.parse(day.maghrib),
                    ishaa = LocalTime.parse(day.isha)
                )
            }
            .sortedBy { it.date }
    }

    @Serializable
    private data class Response(
        val data: Data
    )

    @Serializable
    private data class Data(
        val months: Map<String, Month>
    )

    @Serializable
    private data class Month(
        val days: Map<String, Day>
    )

    @Serializable
    private data class Day(
        val day: Int,
        val month: Int,
        val fajr: String,
        val sunrise: String,
        val dhuhr: String,
        val asr: String,
        val maghrib: String,
        val isha: String
    )
}
