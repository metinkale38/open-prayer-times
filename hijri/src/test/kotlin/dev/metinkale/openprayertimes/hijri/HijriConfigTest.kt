package dev.metinkale.openprayertimes.hijri

import kotlinx.datetime.LocalDate
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HijriConfigTest {
    @BeforeTest
    fun resetBefore() = HijriConfig.reset()

    @AfterTest
    fun resetAfter() = HijriConfig.reset()

    @Test
    fun defaultsToDiyanetWithoutOffset() {
        assertEquals(HijriConfiguration(), HijriConfig.current)

        val date = LocalDate(2022, 11, 16)
        assertEquals(HijriDate.of(1444, 4, 21), date.toHijriDate())
        assertEquals(date, HijriDate.of(1444, 4, 21).toLocalDate())
    }

    @Test
    fun switchesToUmmulQura() {
        val date = LocalDate(2022, 11, 16)
        assertEquals(
            HijriDate.of(1444, 4, 22),
            date.toHijriDate(HijriCalculation.UmmulQura)
        )
        assertEquals(
            date,
            HijriDate.of(1444, 4, 22).toLocalDate(HijriCalculation.UmmulQura)
        )
    }

    @Test
    fun appliesDayOffset() {
        val date = LocalDate(2022, 11, 16)
        assertEquals(
            HijriDate.of(1444, 4, 22),
            date.toHijriDate(HijriCalculation.Diyanet, 1)
        )
        assertEquals(
            date,
            HijriDate.of(1444, 4, 22).toLocalDate(HijriCalculation.Diyanet, 1)
        )
        assertEquals(
            HijriDate.of(1444, 4, 20),
            date.toHijriDate(HijriCalculation.Diyanet, -1)
        )
    }

    @Test
    fun usesGlobalConfiguration() {
        HijriConfig.configure(HijriCalculation.UmmulQura, 1)

        val date = LocalDate(2022, 11, 16)
        assertEquals(
            date.toHijriDate(HijriCalculation.UmmulQura, 1),
            date.toHijriDate()
        )
        assertEquals(HijriDate.of(1444, 4, 23), date.toHijriDate())
    }
}
