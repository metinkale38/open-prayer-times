import dev.metinkale.calctimes.CalcTimes
import dev.metinkale.calctimes.Method
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.test.Test

class Tests {
    @Test
    fun testMWL() {
        var pt = CalcTimes(50.9384, 6.9592, 53.0, TimeZone.currentSystemDefault(), Method.UOIF  )

        pt.getPrayerTimes(LocalDate(2025, 7, 30)).also {
            println(it)
        }
    }


}