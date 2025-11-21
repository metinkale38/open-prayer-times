package dev.metinkale.openprayertimes.sources

import dev.metinkale.openprayertimes.DayTimes
import dev.metinkale.openprayertimes.httpClient
import dev.metinkale.openprayertimes.sources.features.IterableSource
import dev.metinkale.openprayertimes.utils.now
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.plus
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class SemerkandImpl internal constructor() : Source, IterableSource {
    override val name: String = "Semerkand"
    private val json = Json { ignoreUnknownKeys = true }
    override suspend fun getDayTimes(key: String): List<DayTimes> {
        val year = LocalDate.now().year
        val doy = LocalDate.now().dayOfYear
        return httpClient.post("https://www.semerkandtakvimi.com/Home/" + if (key[0] == 'c') "CityTimeList" else "DistricTimeList") {
            cookie("timeZone", "1")
            contentType(ContentType.parse("application/x-www-form-urlencoded; charset=UTF-8"))
            setBody((if (key[0] == 'c') "City=" else "distric=") + key.substring(1) + "&Year=$year&Day=$doy")
        }.bodyAsText().let { json.decodeFromString(ListSerializer(Day.serializer()), it) }.map {
            DayTimes(
                date = LocalDate(year, 1, 1).plus(it.DayOfYear - 1, DateTimeUnit.DAY),
                fajr = it.Fajr.parseTime(),
                sun = it.Tulu.parseTime(),
                dhuhr = it.Zuhr.parseTime(),
                asr = it.Asr.parseTime(),
                maghrib = it.Maghrib.parseTime(),
                ishaa = it.Isha.parseTime(),
                isFajrEstimated = it.Fajr.startsWith("*"),
                isIshaaEstimated = it.Isha.startsWith("*")
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
