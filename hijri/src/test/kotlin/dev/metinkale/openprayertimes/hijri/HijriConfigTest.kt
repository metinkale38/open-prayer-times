package dev.metinkale.openprayertimes.hijri

import kotlinx.datetime.LocalDate
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HijriConfigTest {
    @BeforeTest
    fun resetBefore() {
        HijriConfig.current = HijriConfiguration()
    }

    @AfterTest
    fun resetAfter() {
        HijriConfig.current = HijriConfiguration()
    }

    @Test
    fun defaultsToDiyanetWithoutOffset() {
        assertEquals(HijriConfiguration(), HijriConfig.current)

        val date = LocalDate(2022, 11, 16)
        assertEquals(HijriDate.of(1444, 4, 21), date.toHijriDate())
        assertEquals(date, HijriDate.of(1444, 4, 21).toLocalDate())
    }

    @Test
    fun switchesToUmmulQura() {
        HijriConfig.current = HijriConfiguration(HijriCalculation.UmmulQura)

        val date = LocalDate(2022, 11, 16)
        assertEquals(HijriDate.of(1444, 4, 22), date.toHijriDate())
        assertEquals(date, HijriDate.of(1444, 4, 22).toLocalDate())
    }

    @Test
    fun appliesPositiveDayOffset() {
        HijriConfig.current = HijriConfiguration(HijriCalculation.Diyanet, 1)

        val date = LocalDate(2022, 11, 16)
        assertEquals(HijriDate.of(1444, 4, 22), date.toHijriDate())
        assertEquals(date, HijriDate.of(1444, 4, 22).toLocalDate())
    }

    @Test
    fun appliesNegativeDayOffset() {
        HijriConfig.current = HijriConfiguration(HijriCalculation.Diyanet, -1)

        val date = LocalDate(2022, 11, 16)
        assertEquals(HijriDate.of(1444, 4, 20), date.toHijriDate())
    }

    @Test
    fun combinesCalculationAndOffset() {
        HijriConfig.current = HijriConfiguration(HijriCalculation.UmmulQura, 1)

        val date = LocalDate(2022, 11, 16)
        assertEquals(HijriDate.of(1444, 4, 23), date.toHijriDate())
        assertEquals(date, HijriDate.of(1444, 4, 23).toLocalDate())
    }
}
