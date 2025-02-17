package dev.metinkale.prayertimes.providers.sources

import dev.metinkale.prayertimes.calc.Method
import dev.metinkale.prayertimes.calc.PrayTimes
import dev.metinkale.prayertimes.providers.DayTimes
import dev.metinkale.prayertimes.providers.Entry
import dev.metinkale.prayertimes.providers.geo.Geolocation
import dev.metinkale.prayertimes.providers.sources.features.ByLocationFeature
import dev.metinkale.prayertimes.providers.utils.now
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

internal object Calc : Source, ByLocationFeature {
    override suspend fun search(geolocation: Geolocation): Entry? =
        Entry(
            id = PrayTimes(
                geolocation.lat,
                geolocation.lng,
                geolocation.elv,
                geolocation.timezone,
                Method.MWL
            ).serialize(),
            lat = geolocation.lat,
            lng = geolocation.lng,
            timeZone = geolocation.timezone,
            country = geolocation.country_code,
            names = listOf(mapOf("" to geolocation.name)),
            source = Calc
        )


    override val name: String = "Calc"

    override suspend fun getDayTimes(key: String): List<DayTimes> = getDayTimes(PrayTimes.deserialize(key))

    override suspend fun getDayTime(key: String, day: LocalDate): DayTimes = getDayTime(PrayTimes.deserialize(key), day)

    fun getDayTimes(key: PrayTimes): List<DayTimes> {
        val today = LocalDate.now()
        val monthBegin = LocalDate(today.year, today.monthNumber, 1)
        return (0..365).map { getDayTime(key, monthBegin.plus(it, DateTimeUnit.DAY)) }
    }

    fun getDayTime(key: PrayTimes, day: LocalDate): DayTimes {
        return key.getTimes(day).let {
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
            )
        }
    }
}
