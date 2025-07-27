import dev.metinkale.praytimes.HighLatsAdjustment
import dev.metinkale.praytimes.Method
import dev.metinkale.praytimes.PrayTimes
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.LocalDate
import kotlinx.datetime.UtcOffset
import kotlin.test.Test
import kotlin.test.assertEquals

class Tests {

    @Test
    fun testPrayTimesSerialization() {
        listOf(
            PrayTimes(43.0, -80.0, 0.0, FixedOffsetTimeZone(UtcOffset(-5)), Method.MWL),
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
            assertEquals(it.zawal.toString(), "12:03")
            assertEquals(it.dhuhr.toString(), "12:03")
            assertEquals(it.asrShafi.toString(), "14:48")
            assertEquals(it.asrHanafi.toString(), "15:30")
            assertEquals(it.sunset.toString(), "17:13")
            assertEquals(it.maghrib.toString(), "17:13")
            assertEquals(it.ishaa.toString(), "18:43")
            assertEquals(it.midnight.toString(), "00:03")
        }
    }


}