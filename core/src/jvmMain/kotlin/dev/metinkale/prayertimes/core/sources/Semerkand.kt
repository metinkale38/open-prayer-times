package dev.metinkale.prayertimes.core.sources

import dev.metinkale.prayertimes.core.DayTimes
import dev.metinkale.prayertimes.core.httpClient
import dev.metinkale.prayertimes.core.sources.features.CityListFeature
import dev.metinkale.prayertimes.core.utils.now
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.plus
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

internal object Semerkand : Source, CityListFeature {
    override val name: String = "Semerkand"
    override suspend fun getDayTimes(key: String): List<DayTimes> {
        val year = LocalDate.now().year
        return httpClient.get(
            "https://semerkandtakvimi.semerkandmobile.com/salaattimes?year=" + year + "&" + (if (key[0] == 'c') "cityId=" else "districtId=") +
                    key.substring(1)
        ).bodyAsText().let { Json.decodeFromString(ListSerializer(Day.serializer()), it) }.map {
            DayTimes(
                date = LocalDate(year, 1, 1).plus(it.DayOfYear - 1, DateTimeUnit.DAY),
                fajr = it.Fajr.parseTime(),
                sun = it.Tulu.parseTime(),
                dhuhr = it.Zuhr.parseTime(),
                asr = it.Asr.parseTime(),
                maghrib = it.Maghrib.parseTime(),
                ishaa = it.Isha.parseTime()
            )
        }
    }

    @Serializable
    private class Day(
        val DayOfYear: Int,
        val Fajr: String,
        val Tulu: String,
        val Zuhr: String,
        val Asr: String,
        val Maghrib: String,
        val Isha: String
    )


    // toLocalTime() seems not to work so we do it here
    private fun String.parseTime() = trimStart('*').split(":").let { LocalTime(it[0].toInt(), it[1].toInt()) }
}
