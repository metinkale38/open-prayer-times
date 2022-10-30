package com.metinkale.prayertimes.core.sources

import com.metinkale.prayertimes.core.DayTimes
import com.metinkale.prayertimes.core.Entry
import com.metinkale.prayertimes.core.Secrets
import com.metinkale.prayertimes.core.client
import com.metinkale.prayertimes.core.sources.features.SearchFeature
import com.metinkale.prayertimes.core.utils.now
import com.metinkale.prayertimes.core.utils.toDMY
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


object IGMG : Source("IGMG"), SearchFeature {

    private val json = Json { ignoreUnknownKeys = true }
    suspend fun getDayTimes(id: String, from: LocalDate, to: LocalDate): List<DayTimes> {
        val list = client.get(
            ("https://live.igmgapp.org:8091/api/Calendar/GetPrayerTimes" +
                    "?cityID=" + id +
                    "&from=" + from.toDMY() +
                    "&to=" + to.toDMY()).also { println(it) }
        ) {
            header("X-API-Key", Secrets.igmgApiKey)
        }.bodyAsText().let { json.decodeFromString(PrayerTimesResponse.serializer(), it) }.list


        return list.map {
            DayTimes(
                date = it.date.split(".").reversed().joinToString("-").toLocalDate(),
                fajr = it.fajr.toLocalTime(),
                sun = it.sunrise.toLocalTime(),
                dhuhr = it.dhuhr.toLocalTime(),
                asr = it.dhuhr.toLocalTime(),
                maghrib = it.maghrib.toLocalTime(),
                ishaa = it.ishaa.toLocalTime()
            )
        }

    }

    override suspend fun getDayTimes(id: String): List<DayTimes> {
        val from: LocalDate = LocalDate.now().let { LocalDate(it.year, it.monthNumber, 1) }
        val to: LocalDate = from.plus(1, DateTimeUnit.YEAR)
        return getDayTimes(id, from, to)
    }

    override suspend fun getDayTime(id: String, day: LocalDate): DayTimes? =
        getDayTimes(id = id, from = day, to = day).firstOrNull()



    override fun getCities(): Sequence<Entry> = readTSV(this,"/tsv/igmg.tsv")

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
