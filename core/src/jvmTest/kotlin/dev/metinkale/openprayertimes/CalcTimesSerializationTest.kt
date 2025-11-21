package dev.metinkale.openprayertimes

import dev.metinkale.calctimes.CalcTimes
import dev.metinkale.calctimes.HighLatsAdjustment
import dev.metinkale.calctimes.Method
import dev.metinkale.openprayertimes.sources.CalcImpl
import dev.metinkale.openprayertimes.sources.Source
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.UtcOffset
import kotlin.test.Test
import kotlin.test.assertEquals

class CalcTimesSerializationTest {
    @Test
    fun testPrayTimesSerialization() {
        listOf(
            CalcTimes(43.0, -80.0, 0.0, FixedOffsetTimeZone(UtcOffset(-5)), Method.MWL),
            CalcTimes(
                43.0, -80.0, 0.0, FixedOffsetTimeZone(UtcOffset(-5)),
                Method.MWL.copy(highLats = HighLatsAdjustment.NightMiddle)
            )
        ).forEach {
            val abc = Source.Calc.serializeCalcTimes(it)
            val pt = Source.Calc.deserializeCalcTimes(abc)
            assertEquals(it, pt)
        }
    }
}