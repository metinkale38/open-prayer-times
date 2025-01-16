package dev.metinkale.prayertimes.core.sources

import dev.metinkale.prayertimes.core.Configuration
import dev.metinkale.prayertimes.core.DayTimes
import dev.metinkale.prayertimes.core.cached
import dev.metinkale.prayertimes.core.httpClient
import dev.metinkale.prayertimes.core.sources.features.CityListFeature
import dev.metinkale.prayertimes.core.utils.now
import dev.metinkale.prayertimes.core.utils.toDMY
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


internal object IGMG : Source, CityListFeature {

    override val name: String = "IGMG"
    private val json = Json { ignoreUnknownKeys = true }


    suspend fun getDayTimes(key: String, from: LocalDate, to: LocalDate): List<DayTimes> = cached("$key-$from-$to") {
        val list = httpClient.get(
            ("https://live.igmgapp.org:8091/api/Calendar/GetPrayerTimes" +
                    "?cityID=" + key +
                    "&from=" + from.toDMY() +
                    "&to=" + to.toDMY())
        ) {
            header("X-API-Key", Configuration.IGMG_API_KEY)
        }.bodyAsText().let { json.decodeFromString(PrayerTimesResponse.serializer(), it) }.list


        list.map {
            DayTimes(
                date = it.date.split(".").reversed().joinToString("-").let(LocalDate::parse),
                fajr = it.fajr.let(LocalTime::parse),
                sun = it.sunrise.let(LocalTime::parse),
                dhuhr = it.dhuhr.let(LocalTime::parse),
                asr = it.asr.let(LocalTime::parse),
                maghrib = it.maghrib.let(LocalTime::parse),
                ishaa = it.ishaa.let(LocalTime::parse)
            )
        }

    }

    override suspend fun getDayTimes(key: String): List<DayTimes> {
        val from: LocalDate = LocalDate.now().let { LocalDate(it.year, it.monthNumber, 1) }
        val to: LocalDate = from.plus(1, DateTimeUnit.YEAR)
        return getDayTimes(key, from, to)
    }

    override suspend fun getDayTime(key: String, day: LocalDate): DayTimes? =
        getDayTimes(key = key, from = day, to = day.plus(1, DateTimeUnit.DAY)).firstOrNull()


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
