package dev.metinkale.openprayertimes.sources

import dev.metinkale.calctimes.Method
import dev.metinkale.calctimes.CalcTimes
import dev.metinkale.calctimes.HighLatsAdjustment
import dev.metinkale.calctimes.MethodBuilder
import dev.metinkale.calctimes.Midnight
import dev.metinkale.openprayertimes.DayTimes
import dev.metinkale.openprayertimes.Entry
import dev.metinkale.openprayertimes.geo.Geolocation
import dev.metinkale.openprayertimes.sources.features.LocatableSource
import dev.metinkale.openprayertimes.utils.now
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.plus

class CalcImpl internal constructor() : Source, LocatableSource {
    override suspend fun search(geolocation: Geolocation): Entry? =
        Entry(
            id = serializeCalcTimes(
                CalcTimes(
                    geolocation.lat,
                    geolocation.lng,
                    geolocation.elv,
                    geolocation.timezone,
                    Method.MWL
                )
            ),
            lat = geolocation.lat,
            lng = geolocation.lng,
            timeZone = geolocation.timezone,
            country = geolocation.country_code,
            names = listOf(mapOf("" to geolocation.name)),
            source = Source.Calc
        )


    override val name: String = "Calc"

    override suspend fun getDayTimes(key: String): List<DayTimes> = getDayTimes(deserializeCalcTimes(key))

    override suspend fun getDayTime(key: String, day: LocalDate): DayTimes = getDayTime(deserializeCalcTimes(key), day)

    fun getDayTimes(key: CalcTimes): List<DayTimes> {
        val today = LocalDate.now()
        val monthBegin = LocalDate(today.year, today.month.number, 1)
        return (0..365).map { getDayTime(key, monthBegin.plus(it, DateTimeUnit.DAY)) }
    }

    fun getDayTime(key: CalcTimes, day: LocalDate): DayTimes {
        return key.getPrayerTimes(day).let {
            DayTimes(
                date = day,
                fajr = it.imsak,
                sun = it.sunrise,
                dhuhr = it.dhuhr,
                asr = it.asrShafi,
                asrHanafi = it.asrHanafi,
                maghrib = it.maghrib,
                ishaa = it.ishaa,
                sabah = it.fajr,
                isFajrEstimated = it.usedHighLatsAdjustmentImsak,
                isIshaaEstimated = it.usedHighLatsAdjustmentIshaa
            )
        }
    }

    fun serializeCalcTimes(value: CalcTimes): String = listOf(
        "${value.latitude}",
        "${value.longitude}",
        value.elevation.takeIf { it != 0.0 }?.let { "$it" } ?: "",
        value.timezone.id.replace("/", "::"),
        serializeMethod(value.method))
        .joinToString(";")

    fun deserializeCalcTimes(key: String): CalcTimes = key.split(";").let {
        CalcTimes(
            it[0].toDouble(),
            it[1].toDouble(),
            it[2].toDoubleOrNull() ?: 0.0,
            TimeZone.of(it[3].replace("::", "/")),
            deserializeMethod(it.drop(4).joinToString(";"))
        )
    }

    private fun serializeMethod(method: Method): String = method.shortName ?: listOf(
        method.highLats.name,
        method.midnight.name,
        method.imsakAngle?.toString(),
        method.imsakMinute.takeIf { it != 0 }?.toString(),
        method.fajrAngle?.toString(),
        method.fajrMinute.takeIf { it != 0 }?.toString(),
        method.sunriseMinute.takeIf { it != 0 }?.toString(),
        method.dhuhrMinute.takeIf { it != 0 }?.toString(),
        method.asrShafiMinute.takeIf { it != 0 }?.toString(),
        method.asrHanafiMinute.takeIf { it != 0 }?.toString(),
        method.sunsetMinutes.takeIf { it != 0 }?.toString(),
        method.maghribAngle?.toString(),
        method.maghribMinute.takeIf { it != 0 }?.toString(),
        method.ishaaAngle?.toString(),
        method.ishaaMinute.takeIf { it != 0 }?.toString()
    ).joinToString(";") { it.orEmpty() }

    private fun deserializeMethod(value: String): MethodBuilder =
        Method.values().find { it.shortName == value } ?: value.split(";").let {
            Method(
                highLats = HighLatsAdjustment.valueOf(it[0]),
                midnight = Midnight.valueOf(it[1]),
                imsakAngle = it[2].toDoubleOrNull(),
                imsakMinute = it[3].toIntOrNull() ?: 0,
                fajrAngle = it[4].toDoubleOrNull(),
                fajrMinute = it[5].toIntOrNull() ?: 0,
                sunriseMinute = it[6].toIntOrNull() ?: 0,
                dhuhrMinute = it[7].toIntOrNull() ?: 0,
                asrShafiMinute = it[8].toIntOrNull() ?: 0,
                asrHanafiMinute = it[9].toIntOrNull() ?: 0,
                sunsetMinutes = it[10].toIntOrNull() ?: 0,
                maghribAngle = it[11].toDoubleOrNull(),
                maghribMinute = it[12].toIntOrNull() ?: 0,
                ishaaAngle = it[13].toDoubleOrNull(),
                ishaaMinute = it[14].toIntOrNull() ?: 0
            )
        }
}

