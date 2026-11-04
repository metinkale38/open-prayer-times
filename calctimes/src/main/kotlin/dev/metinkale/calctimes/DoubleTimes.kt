package dev.metinkale.calctimes

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round
import kotlin.time.Duration.Companion.minutes


internal data class DoubleTimes(
    var imsak: Double,
    var fajr: Double,
    var sunrise: Double,
    var zawal: Double,
    var dhuhr: Double,
    var asrShafi: Double,
    var asrHanafi: Double,
    var sunset: Double,
    var maghrib: Double,
    var ishaa: Double,
    var firstThird: Double,
    var midnight: Double,
    var lastThird: Double,
    var usedHighLatsAdjustmentFajr: Boolean,
    var usedHighLatsAdjustmentImsak: Boolean,
    var usedHighLatsAdjustmentMaghrib: Boolean,
    var usedHighLatsAdjustmentIshaa: Boolean
) {
    fun updateEach(block: (Double) -> Double) {
        imsak = block(imsak)
        fajr = block(fajr)
        sunrise = block(sunrise)
        zawal = block(zawal)
        dhuhr = block(dhuhr)
        asrShafi = block(asrShafi)
        asrHanafi = block(asrHanafi)
        sunset = block(sunset)
        maghrib = block(maghrib)
        ishaa = block(ishaa)
        firstThird = block(firstThird)
        midnight = block(midnight)
        lastThird = block(lastThird)
    }

    fun toTimes(date: LocalDate, timezone: TimeZone): PrayerTimes {
        fun Double.toLocalTime(roundUp: Boolean?): LocalTime {
            if (isNaN()) return LocalTime(0, 0)

            val totalMinutes = when (roundUp) {
                true -> ceil(this * 60.0).toInt()
                false -> floor(this * 60.0).toInt()
                null -> round(this * 60.0).toInt()
            }

            return (date.atStartOfDayIn(TimeZone.UTC) + totalMinutes.minutes)
                .toLocalDateTime(timezone).time
        }

        return PrayerTimes(
            imsak = imsak.toLocalTime(false),
            fajr = fajr.toLocalTime(false),
            sunrise = sunrise.toLocalTime(false),
            zawal = zawal.toLocalTime(null),
            dhuhr = dhuhr.toLocalTime(true),
            asrShafi = asrShafi.toLocalTime(true),
            asrHanafi = asrHanafi.toLocalTime(true),
            sunset = sunset.toLocalTime(null),
            maghrib = maghrib.toLocalTime(true),
            ishaa = ishaa.toLocalTime(true),
            midnight = midnight.toLocalTime(null),
            firstThird = firstThird.toLocalTime(null),
            lastThird = lastThird.toLocalTime(null),
            usedHighLatsAdjustmentFajr = usedHighLatsAdjustmentFajr,
            usedHighLatsAdjustmentImsak = usedHighLatsAdjustmentImsak,
            usedHighLatsAdjustmentMaghrib = usedHighLatsAdjustmentMaghrib,
            usedHighLatsAdjustmentIshaa = usedHighLatsAdjustmentIshaa
        )
    }
}
