import dev.metinkale.prayertimes.calc.HighLatsAdjustment
import dev.metinkale.prayertimes.calc.Method
import dev.metinkale.prayertimes.calc.PrayTimes
import dev.metinkale.prayertimes.calc.Times
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import org.junit.Test
import kotlin.math.abs
import kotlin.test.assertEquals

class Tests {

    @Test
    fun testPrayTimesSerialization() {
        listOf(
            PrayTimes(43.0, -80.0, 0.0, FixedOffsetTimeZone(UtcOffset(-5)), Method.MWL),
            PrayTimes(43.0, -80.0, 0.0, FixedOffsetTimeZone(UtcOffset(-5)), Method.IGMG),
            PrayTimes(
                43.0, -80.0, 0.0, FixedOffsetTimeZone(UtcOffset(-5)),
                Method.MWL.copy(highLats = HighLatsAdjustment.NightMiddle)
            )
        ).forEach {
            val pt = PrayTimes.deserialize(it.serialize())
            assertEquals(it, pt)
        }

    }

    @Test
    fun testMWL() {
        val pt = PrayTimes(43.0, -80.0, 0.0, FixedOffsetTimeZone(UtcOffset(-5)), Method.MWL)

        pt.getTimes(LocalDate(2022, 10, 31)).also {
            assertEquals(it.imsak.toString(), "05:17")
            assertEquals(it.fajr.toString(), "05:17")
            assertEquals(it.sunrise.toString(), "06:53")
            assertEquals(it.zawal.toString(), "12:04")
            assertEquals(it.dhuhr.toString(), "12:04")
            assertEquals(it.asrShafi.toString(), "14:49")
            assertEquals(it.asrHanafi.toString(), "15:31")
            assertEquals(it.sunset.toString(), "17:13")
            assertEquals(it.maghrib.toString(), "17:13")
            assertEquals(it.ishaa.toString(), "18:44")
            assertEquals(it.midnight.toString(), "00:03")
        }
    }


    @Test
    fun testIGMG() {
        val lat = 52.266666
        val lng = 10.516667
        val pt = PrayTimes(lat, lng, 0.0, TimeZone.of("UTC+1"), Method.IGMG)

        pt.getTimes(LocalDate(2022, 10, 31)).also {
            assertEquals(it.fajr.toString(), "05:46")
            assertEquals(it.sunrise.toString(), "07:06")
            assertEquals(it.dhuhr.toString(), "12:07")
            assertEquals(it.asrShafi.toString(), "14:29")
            assertEquals(it.maghrib.toString(), "16:57")
            assertEquals(it.ishaa.toString(), "18:07")
        }
    }

}