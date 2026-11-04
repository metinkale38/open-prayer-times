import dev.metinkale.calctimes.CalcTimes
import dev.metinkale.calctimes.Method
import dev.metinkale.calctimes.Midnight
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals

class CalculationTest {

    @Test
    fun testMethodPresets() {
        assertEquals(15.0, Method.ISNA.fajrAngle)
        assertEquals(18.0, Method.Karachi.ishaaAngle)

        Method.values().filterIsInstance<Method>().forEach {
            assertEquals(Midnight.SunsetFajr, it.midnight, "${it.shortName} midnight")
        }

        val malaysia = Method.values().filterIsInstance<Method>().first { it.shortName == "Malaysia" }
        assertEquals(20.0, malaysia.fajrAngle)
        assertEquals(18.0, malaysia.ishaaAngle)
    }

    @Test
    fun testPerPrayerRounding() {
        val times = CalcTimes(
            52.52,
            13.405,
            0.0,
            TimeZone.of("Europe/Berlin"),
            Method.MWL
        ).getPrayerTimes(LocalDate(2025, 7, 30))

        assertEquals(LocalTime(2, 8), times.fajr)
        assertEquals(LocalTime(5, 23), times.sunrise)
        assertEquals(LocalTime(13, 13), times.dhuhr)
        assertEquals(LocalTime(17, 26), times.asrShafi)
        assertEquals(LocalTime(21, 2), times.sunset)
        assertEquals(LocalTime(21, 2), times.maghrib)
        assertEquals(LocalTime(23, 52), times.ishaa)
        assertEquals(LocalTime(22, 44), times.firstThird)
        assertEquals(LocalTime(23, 35), times.midnight)
        assertEquals(LocalTime(0, 26), times.lastThird)
    }
}
