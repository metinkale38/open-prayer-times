package dev.metinkale.prayertimes.core.sources

import dev.metinkale.prayertimes.calc.Method
import dev.metinkale.prayertimes.calc.PrayTimes
import dev.metinkale.prayertimes.core.DayTimes
import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.Geocoder
import dev.metinkale.prayertimes.core.sources.features.ByLocationFeature
import dev.metinkale.prayertimes.core.sources.features.DayTimesFeature
import dev.metinkale.prayertimes.core.utils.now
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

object Calc : Source, DayTimesFeature, ByLocationFeature {
    override suspend fun search(geolocation: Geocoder.GeoLocation): List<Entry> {
        val tz = Geocoder.getTimeZone(geolocation.lat, geolocation.lng)
        val elv = Geocoder.getElevation(geolocation.lat, geolocation.lng)
        return Method.values().map {
            Entry(
                id = PrayTimes(geolocation.lat, geolocation.lng, elv, tz, it).serialize(),
                lat = geolocation.lat,
                lng = geolocation.lng,
                timeZone = tz,
                country = geolocation.country,
                names = geolocation.name.map { mapOf("" to it) },
                source = Calc
            )
        }
    }


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
