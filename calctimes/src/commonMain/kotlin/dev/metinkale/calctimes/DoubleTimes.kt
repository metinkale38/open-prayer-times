package dev.metinkale.calctimes

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.floor


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
        fun Double.toLocalTime(): LocalTime {
            return (if (isNaN()) {
                LocalTime(0, 0)
            } else {
                date.atTime(
                    floor(this).toInt(), floor((this - floor(this)) * 60).toInt()
                ).toInstant(TimeZone.UTC).toLocalDateTime(timezone).time
            })
        }

        return PrayerTimes(
            imsak = imsak.toLocalTime(),
            fajr = fajr.toLocalTime(),
            sunrise = sunrise.toLocalTime(),
            zawal = zawal.toLocalTime(),
            dhuhr = dhuhr.toLocalTime(),
            asrShafi = asrShafi.toLocalTime(),
            asrHanafi = asrHanafi.toLocalTime(),
            sunset = sunset.toLocalTime(),
            maghrib = maghrib.toLocalTime(),
            ishaa = ishaa.toLocalTime(),
            midnight = midnight.toLocalTime(),
            firstThird = firstThird.toLocalTime(),
            lastThird = lastThird.toLocalTime(),
            usedHighLatsAdjustmentFajr = usedHighLatsAdjustmentFajr,
            usedHighLatsAdjustmentImsak = usedHighLatsAdjustmentImsak,
            usedHighLatsAdjustmentMaghrib = usedHighLatsAdjustmentMaghrib,
            usedHighLatsAdjustmentIshaa = usedHighLatsAdjustmentIshaa
        )
    }
}
