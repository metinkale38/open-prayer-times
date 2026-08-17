package dev.metinkale.openprayertimes.server.provider

import dev.metinkale.openprayertimes.DayTimes
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDate

object IGMGProvider : PrayerTimesFetcher {
    private val json = Json { ignoreUnknownKeys = true }
    private fun LocalDate.toDMY() = toString().split("-").reversed().joinToString(".")

    override suspend fun getPrayerTimes(key: String): List<DayTimes> {
        val from = LocalDate.now().let { it.withDayOfMonth(1) }
        val to = from.withMonth(12).withDayOfMonth(31)

        val list = httpClient.get(
            ("https://live.igmgapp.org:8091/api/Calendar/GetPrayerTimes" +
                    "?cityID=" + key +
                    "&from=" + from.toDMY() +
                    "&to=" + to.toDMY())
        ) {
            header("X-API-Key", System.getenv("IGMG_API_KEY"))
        }.bodyAsText().let { json.decodeFromString(PrayerTimesResponse.serializer(), it) }.list


        return list.map {
            DayTimes(
                date = LocalDate.parse(it.date.split(".").reversed().joinToString("-")).toKotlinLocalDate(),
                fajr = LocalTime.parse(it.fajr),
                sun = LocalTime.parse(it.sunrise),
                dhuhr = LocalTime.parse(it.dhuhr),
                asr = LocalTime.parse(it.asr),
                maghrib = LocalTime.parse(it.maghrib),
                ishaa = LocalTime.parse(it.ishaa),
            )
        }
    }


    @Serializable
    internal data class PrayerTimesResponse(
        var list: List<PrayerTimesEntry>
    )

    @Serializable
    internal data class PrayerTimesEntry(
        val date: String,
        val fajr: String,
        val sunrise: String,
        val dhuhr: String,
        val asr: String,
        val maghrib: String,
        val ishaa: String
    )
}