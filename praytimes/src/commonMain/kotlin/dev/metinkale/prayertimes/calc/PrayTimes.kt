package dev.metinkale.prayertimes.calc

import kotlinx.datetime.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sqrt


@Serializable(with = PrayTimes.Companion.Serializer::class)
data class PrayTimes(
    val latitude: Double, val longitude: Double, val elevation: Double, val timezone: TimeZone, val method: Method
) {
    constructor(
        latitude: Double, longitude: Double, elevation: Double, timezone: TimeZone, method: MethodBuilder
    ) : this(latitude, longitude, elevation, timezone, method.build(latitude, longitude, elevation))


    /**
     * calculate prayer times for a given date
     */
    fun getTimes(date: LocalDate): Times<LocalTime> {
        val times = getTimesAsDouble(date)
        return Times.withDefault(LocalTime(0, 0)).also { result ->
            val prop1 = Times.properties<Double>()
            val prop2 = Times.properties<LocalTime>()

            prop1.map { it.get(times) }.forEachIndexed { index, value ->
                val time = if (value.isNaN()) LocalTime(0, 0) else date.atTime(
                    floor(value).toInt(),
                    floor((value - floor(value)) * 60).toInt()
                ).toInstant(TimeZone.UTC).toLocalDateTime(timezone).time
                prop2[index].set(result, time)
            }
        }
    }


    internal fun getTimesAsDouble(date: LocalDate): Times<Double> {
        val jdate = julian(date.year, date.monthNumber, date.dayOfMonth) - longitude / (15.0 * 24.0)

        val times = Times(5.0, 5.0, 6.0, 12.0, 12.0, 13.0, 13.0, 18.0, 18.0, 18.0, 0.0)

        // convert to day portions
        Times.properties<Double>().forEach { it.set(times, it.get(times) / 24.0) }


        // first all angle based calculations are done

        //Imsak: if angle is 0, use sunrise, given angle otherwise (if angle is 0, calculation is probably minute based)
        times.imsak = sunAngleTime(jdate, method.imsakAngle ?: sunriseAngle(), times.imsak, true)
        //Fajr: if angle is 0, use sunrise, angle otherwise (if angle is 0, calculation is probably minute based)
        times.fajr = sunAngleTime(jdate, method.fajrAngle ?: sunriseAngle(), times.fajr, true)
        // Sunrise: fix calculation
        times.sunrise = sunAngleTime(jdate, sunriseAngle(), times.sunrise, true)
        // Zawal: fix calculation
        times.zawal = midDay(jdate, times.zawal)
        // Dhuhr: fix calculation
        times.dhuhr = midDay(jdate, times.dhuhr)
        // Asr Shafi: fix calculation, shadow factor 1
        times.asrShafi = asrTime(jdate, 1, times.asrShafi)
        // Asr Hanafi: fix calculation, shadow factor 2
        times.asrHanafi = asrTime(jdate, 2, times.asrHanafi)
        // Sunset: fix calculation
        times.sunset = sunAngleTime(jdate, sunsetAngle(), times.sunset, false)
        // Maghrib: if angle is 0, use sunset, given angle otherwise
        times.maghrib = sunAngleTime(jdate, method.maghribAngle ?: sunsetAngle(), times.maghrib, false)
        // Ishaa: if angle is 0, use sunset, given angle otherwise (if angle is 0, calculation is probably minute based)
        times.ishaa = sunAngleTime(jdate, method.ishaaAngle ?: sunsetAngle(), times.ishaa, false)
        // midnight will be calculated later
        // times.midnight = 0;

        if (method.highLats !== HighLatsAdjustment.None) {
            val nightTime: Double = timeDiff(times.sunset, times.sunrise)
            times.imsak = adjustHLTime(times.imsak, times.sunrise, method.imsakAngle ?: 0.0, nightTime, true)
            times.fajr = adjustHLTime(times.fajr, times.sunrise, method.fajrAngle ?: 0.0, nightTime, true)
            times.ishaa = adjustHLTime(times.ishaa, times.sunset, method.ishaaAngle ?: 0.0, nightTime, false)
            times.maghrib = adjustHLTime(times.maghrib, times.sunset, method.maghribAngle ?: 0.0, nightTime, false)
        }


        // add midnight time
        if (method.midnight === Midnight.Standard) {
            times.midnight = times.sunset + timeDiff(times.sunset, times.sunrise) / 2.0
        } else if (method.midnight === Midnight.Jafari) {
            times.midnight = times.sunset + timeDiff(times.sunset, times.fajr) / 2.0
        }

        method.imsakMinute.let { times.imsak += it / 60.0 }
        method.fajrMinute.let { times.fajr += it / 60.0 }
        method.sunriseMinute.let { times.sunrise += it / 60.0 }
        method.dhuhrMinute.let { times.dhuhr += it / 60.0 }
        method.asrShafiMinute.let { times.asrShafi += it / 60.0 }
        method.asrHanafiMinute.let { times.asrHanafi += it / 60.0 }
        method.sunsetMinutes.let { times.sunset += it / 60.0 }
        method.maghribMinute.let { times.maghrib += it / 60.0 }
        method.ishaaMinute.let { times.ishaa += it / 60.0 }

        Times.properties<Double>().forEach {
            var time = it.get(times)
            time -= longitude / 15.0
            while (time > 24) time -= 24.0
            while (time < 0) time += 24.0
            it.set(times, time)
        }


        return times
    }

    /**
     * compute the difference between two times
     *
     * @param time1 Time 1
     * @param time2 Time 2
     * @return timediff
     */
    private fun timeDiff(time1: Double, time2: Double): Double {
        return DMath.fixHour(time2 - time1)
    }


    /**
     * adjust a time for higher latitudes
     *
     *
     * how it works:
     * dependend on the calculation method there is a maximum night portion, that might be used from time to base
     *
     *
     * (e.g.) if NightMiddle is taken and the calculated fajr takes more than the half night, it will be fixed to midnight
     *
     * @param time  time
     * @param base  base
     * @param angle angle
     * @param night night time
     * @param ccw   true if clock-counter-wise, false otherwise
     * @return adjusted time
     */
    private fun adjustHLTime(time: Double, base: Double, angle: Double, night: Double, ccw: Boolean): Double {
        val maxPortion = maxNightPortion(angle)
        val maxDuration = maxPortion * night
        val timeDiff: Double = if (ccw) timeDiff(time, base) else timeDiff(base, time)
        if (time.isNaN() || timeDiff > maxDuration) return base + if (ccw) -maxDuration else maxDuration
        return time
    }

    /**
     * the maximum night portion used for adjusting times in higher latitudes
     *
     * @param angle angle
     * @return night portion
     */
    private fun maxNightPortion(angle: Double): Double {
        return when (method.highLats) {
            HighLatsAdjustment.None -> 1.0
            HighLatsAdjustment.AngleBased -> 1.0 / 60.0 * angle
            HighLatsAdjustment.OneSeventh -> 1.0 / 7.0
            HighLatsAdjustment.NightMiddle -> 1.0 / 2.0
            HighLatsAdjustment.OneThird -> 1.0 / 3.0
            HighLatsAdjustment.OneFifth -> 1.0 / 5.0
        }
    }


    /**
     * compute asr time
     *
     * @param jdate Julian Date
     * @param factor Shadow Factor
     * @param time   default  time
     * @return asr time
     */
    private fun asrTime(jdate: Double, factor: Int, time: Double): Double {
        val decl = sunPositionDeclination(jdate + time)
        val angle = -DMath.arccot(factor + DMath.tan(abs(latitude - decl)))
        return sunAngleTime(jdate, angle, time, false)
    }

    /**
     * compute the time at which sun reaches a specific angle below horizon
     *
     * @param jdate julian date
     * @param angle angle
     * @param time  default time
     * @param ccw   true if counter-clock-wise, false otherwise
     * @return time
     */
    private fun sunAngleTime(jdate: Double, angle: Double, time: Double, ccw: Boolean): Double {
        val decl = sunPositionDeclination(jdate + time)
        val noon = midDay(jdate, time)
        val t = 1.0 / 15.0 * DMath.arccos(
            (-DMath.sin(angle) - DMath.sin(decl) * DMath.sin(latitude)) / (DMath.cos(decl) * DMath.cos(latitude))
        )
        return noon + if (ccw) -t else t
    }

    /**
     * compute mid-day time
     *
     * @param jdate julian date
     * @param time default time
     * @return midday time
     */
    private fun midDay(jdate: Double, time: Double): Double {
        val eqt = equationOfTime(jdate + time)
        return DMath.fixHour(12 - eqt)
    }

    /**
     * compute equation of time
     * Ref: http://aa.usno.navy.mil/faq/docs/SunApprox.php
     *
     * @param jd julian date
     * @return equation of time
     */
    private fun equationOfTime(jd: Double): Double {
        val d = jd - 2451545.0
        val g = DMath.fixAngle(357.529 + 0.98560028 * d)
        val q = DMath.fixAngle(280.459 + 0.98564736 * d)
        val l = DMath.fixAngle(q + 1.915 * DMath.sin(g) + 0.020 * DMath.sin(2 * g))
        val e = 23.439 - 0.00000036 * d
        val ra = DMath.arctan2(DMath.cos(e) * DMath.sin(l), DMath.cos(l)) / 15
        return q / 15.0 - DMath.fixHour(ra)
    }

    /**
     * compute  declination angle of sun
     * Ref: http://aa.usno.navy.mil/faq/docs/SunApprox.php
     *
     * @param jd julian date
     * @return declination angle of sun
     */
    private fun sunPositionDeclination(jd: Double): Double {
        val d = jd - 2451545.0
        val g = DMath.fixAngle(357.529 + 0.98560028 * d)
        val q = DMath.fixAngle(280.459 + 0.98564736 * d)
        val l = DMath.fixAngle(q + 1.915 * DMath.sin(g) + 0.020 * DMath.sin(2 * g))
        val e = 23.439 - 0.00000036 * d
        return DMath.arcsin(DMath.sin(e) * DMath.sin(l))
    }

    /**
     * compute sun angle for sunrise
     *
     * @return sun angle of sunrise
     */
    private fun sunriseAngle(): Double {
        //double earthRad = 6371009; // in meters
        //double angle = DMath.arccos(earthRad/(earthRad+ elv));
        val angle: Double = if (method.useElevation) 0.0347 * sqrt(elevation) else 0.0 // an approximation
        return 0.833 + angle
    }

    /**
     * compute sun angle for sunset
     *
     * @return sun angle of sunset
     */
    private fun sunsetAngle(): Double {
        //val earthRad = 6371009; // in meters
        //val angle = DMath.arccos(earthRad/(earthRad+ elv));
        val angle: Double = if (method.useElevation) 0.0347 * sqrt(elevation) else 0.0 // an approximation
        return 0.833 + angle
    }

    /**
     * convert Gregorian date to Julian day
     * Ref: Astronomical Algorithms by Jean Meeus
     *
     * @param year  year
     * @param month month
     * @param day   day
     * @return julian day
     */
    private fun julian(year: Int, month: Int, day: Int): Double {
        var jyear = year
        var jmonth = month
        if (jmonth <= 2) {
            jyear -= 1
            jmonth += 12
        }
        val a: Double = floor(jyear / 100.0)
        val b: Double = 2 - a + floor(a / 4.0)
        return floor(365.25 * (jyear + 4716)) + floor(30.6001 * (jmonth + 1)) + day + b - 1524.5
    }


    fun serialize(): String = listOf("$latitude",
        "$longitude",
        elevation.takeIf { it != 0.0 }?.let { "$it" } ?: "",
        timezone.id.replace("/", "::"),
        method.serialize()).joinToString(";")

    companion object {
        fun deserialize(key: String): PrayTimes = key.split(";").let {
            PrayTimes(
                it[0].toDouble(),
                it[1].toDouble(),
                it[2].toDoubleOrNull() ?: 0.0,
                TimeZone.of(it[3].replace("::", "/")),
                Method.deserialize(it.drop(4).joinToString(";"))
            )
        }

        object Serializer : KSerializer<PrayTimes> {
            override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PrayTimes", PrimitiveKind.STRING)
            override fun serialize(encoder: Encoder, value: PrayTimes) = encoder.encodeString(value.serialize())
            override fun deserialize(decoder: Decoder): PrayTimes = deserialize(decoder.decodeString())
        }
    }
}
