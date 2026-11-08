package dev.metinkale.openprayertimes.server.provider

import dev.metinkale.openprayertimes.DayTimes
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDate

object JAKIMProvider : PrayerTimesFetcher {
    private val json = Json { ignoreUnknownKeys = true }
    private val months = mapOf(
        "Jan" to 1, "Feb" to 2, "Mac" to 3, "Mar" to 3, "Apr" to 4,
        "Mei" to 5, "May" to 5, "Jun" to 6, "Jul" to 7, "Ogos" to 8,
        "Ogo" to 8, "Aug" to 8, "Sep" to 9, "Okt" to 10, "Oct" to 10, "Nov" to 11,
        "Dis" to 12, "Dec" to 12
    )

    override suspend fun getPrayerTimes(key: String): List<DayTimes> {
        val response = httpClient
            .get("https://www.e-solat.gov.my/index.php?r=esolatApi/takwimsolat&period=year&zone=$key")
            .bodyAsText()
            .let { json.decodeFromString(Response.serializer(), it) }

        return response.prayerTime.map {
            DayTimes(
                date = it.date.parseDate().toKotlinLocalDate(),
                fajr = LocalTime.parse(it.fajr),
                sun = LocalTime.parse(it.syuruk),
                dhuhr = LocalTime.parse(it.dhuhr),
                asr = LocalTime.parse(it.asr),
                maghrib = LocalTime.parse(it.maghrib),
                ishaa = LocalTime.parse(it.isha)
            )
        }.sortedBy { it.date }
    }

    private fun String.parseDate() = split("-").let {
        LocalDate.of(it[2].toInt(), months.getValue(it[1]), it[0].toInt())
    }

    @Serializable
    private data class Response(
        val prayerTime: List<PrayerTime>
    )

    @Serializable
    private data class PrayerTime(
        val date: String,
        val fajr: String,
        val syuruk: String,
        val dhuhr: String,
        val asr: String,
        val maghrib: String,
        val isha: String
    )
}
