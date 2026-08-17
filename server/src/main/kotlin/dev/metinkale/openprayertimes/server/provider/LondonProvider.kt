package dev.metinkale.openprayertimes.server.provider

import dev.metinkale.openprayertimes.DayTimes
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


object LondonProvider : PrayerTimesFetcher {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getPrayerTimes(key: String): List<DayTimes> {
        val response =
            httpClient.get("https://www.londonprayertimes.com/api/times/?format=json&key=${System.getenv("LONDON_PRAYER_TIMES_API_KEY")}&year=" + java.time.LocalDate.now().year)
                .bodyAsText()
                .let { json.decodeFromString(Result.serializer(), it) }


        return response.times?.values?.map {
            val date = LocalDate.parse(it.date!!)
            val times = mutableListOf(
                LocalTime.parse(it.fajr!!),
                LocalTime.parse(it.sunrise!!),
                LocalTime.parse(it.dhuhr!!),
                LocalTime.parse(it.asr!!),
                LocalTime.parse(it.asr_2!!),
                LocalTime.parse(it.magrib!!),
                LocalTime.parse(it.isha!!),
            )

            var last: LocalTime? = null
            times.forEachIndexed { index, time ->
                if (last == null) last = time
                else if (last!! > time) times[index] = LocalTime(time.hour + 12, time.minute)
            }

            DayTimes(
                date,
                fajr = times[0],
                sun = times[1],
                dhuhr = times[2],
                asr = times[3],
                asrHanafi = times[4],
                maghrib = times[5],
                ishaa = times[6]
            )
        } ?: emptyList()
    }


    @Serializable
    private data class Result(
        val times: Map<String, Times>? = null
    )

    @Serializable
    private data class Times(
        val date: String? = null,
        val fajr: String? = null,
        val sunrise: String? = null,
        val dhuhr: String? = null,
        val asr: String? = null,
        val asr_2: String? = null,
        val magrib: String? = null,
        val isha: String? = null
    )
}